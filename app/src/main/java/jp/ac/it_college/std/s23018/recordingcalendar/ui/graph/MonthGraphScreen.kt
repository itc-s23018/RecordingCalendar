package jp.ac.it_college.std.s23018.recordingcalendar.ui.graph

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import jp.ac.it_college.std.s23018.recordingcalendar.RecordingCalendarApplication
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@Composable
fun MonthGraphScreen(
    navController: NavController,
    userInformation: UserEntity? = null
) {

    var selectedMonth by remember { mutableStateOf(LocalDate.now()) }

    //月の最初の月曜日を取得
    val firstDayOfMonth = selectedMonth.withDayOfMonth(1)
    val startOfWeek = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

    //月の最後の日と最後の月曜日を取得
    val lastDayOfMonth = selectedMonth.with(TemporalAdjusters.lastDayOfMonth())
    val lastMondayOfMonth = lastDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    var userInfo by remember {
        mutableStateOf(userInformation)
    }

    var weights by remember {
        mutableStateOf<List<WeightEntity>>(emptyList())
    }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val user_db = app.container.userRepository
    val weight_db = app.container.recordRepository

    val coroutineScope = rememberCoroutineScope()

    fun refreshData() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val fetchedUser = user_db.getUser()
                val startOfMonth = selectedMonth.withDayOfMonth(1)
                val endOfMonth = selectedMonth.with(TemporalAdjusters.lastDayOfMonth())
                val startDate = startOfMonth.format(DateTimeFormatter.ISO_DATE)
                val endDate = endOfMonth.format(DateTimeFormatter.ISO_DATE)
                val weightData = weight_db.getWeightsByMonth(startDate, endDate)
                userInfo = fetchedUser
                weights = weightData
            }
        }
    }

    LaunchedEffect(selectedMonth) {
        refreshData()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { selectedMonth = selectedMonth.minusMonths(1) }) {
            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Month")
        }
        Text(
            text = "${selectedMonth.year}年 ${selectedMonth.monthValue}月",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(onClick = { selectedMonth = selectedMonth.plusMonths(1) }) {
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Month")
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val xAxisStart = Offset(90f, size.height - 100f)
            val xAxisEnd = Offset(size.width - 40f, size.height - 100f)
            drawLine(
                color = Color.Black,
                start = xAxisStart,
                end = xAxisEnd,
                strokeWidth = 5f
            )

            val mondaysInMonth = generateSequence(startOfWeek) { it.plusWeeks(1) }
                .takeWhile { it <= lastMondayOfMonth }
                .toList()

            val xStep = (size.width - 90) / mondaysInMonth.size
            val offset = xStep / 2

            mondaysInMonth.forEachIndexed { index, monday ->
                val xPos = 90f + xStep * index + offset
                val formattedDate = "${monday.monthValue}/${monday.dayOfMonth}"
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        formattedDate,
                        xPos,
                        size.height - 20f,
                        Paint().apply {
                            textSize = 50f
                        }
                    )
                }
            }

            val yAxisStart = Offset(95f, 100f)
            val yAxisEnd = Offset(95f, size.height - 100f)
            drawLine(
                color = Color.Black,
                start = yAxisStart,
                end = yAxisEnd,
                strokeWidth = 5f
            )

            val weightMax = (userInfo?.weight ?: 0f) + 2f
            val weightMin = (userInfo?.weight ?: 0f) - 2f
            val weightRange = weightMax - weightMin
            val yStepHeight = (yAxisEnd.y - yAxisStart.y) / weightRange

            for (i in 0..(weightRange.toInt()) step 1) {
                val weightLabel = weightMin + i
                val yPos = yAxisEnd.y - (i * yStepHeight)
                drawContext.canvas.nativeCanvas.drawText(
                    "${weightLabel.toInt()}",
                    yAxisStart.x - 20f,
                    yPos,
                    Paint().apply {
                        color =
                            if (i == weightRange.toInt() / 2) Color.Blue.toArgb() else Color.Black.toArgb()
                        textSize = 50f
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                )
            }

            val datas = mutableListOf<Offset>()

            mondaysInMonth.forEachIndexed { index, monday ->
                val xPos = 90f + xStep * index + offset

                val weekData = weights.filter { weight ->
                    val weightDate = LocalDate.parse(weight.date)
                    weightDate.isAfter(monday.minusDays(1)) && weightDate.isBefore(
                        monday.plusWeeks(
                            1
                        )
                    )
                }

                //週ごとに平均体重を計算
                val averageWeight = if (weekData.isNotEmpty()) {
                    weekData.map { it.weight }.average().toFloat()
                } else {
                    weightMin
                }

                val weightPos = when {
                    averageWeight < weightMin -> 0.09f
                    averageWeight > weightMax -> (weightMax - weightMin)
                    else -> (averageWeight - weightMin)
                }

                val weightYPos = yAxisEnd.y - (weightPos * yStepHeight)

                val point = Offset(xPos, weightYPos)
                datas.add(point)

                drawCircle(
                    color = if (!weekData.isNotEmpty()) Color.Gray else Color.Black,
                    radius = 20f,
                    center = Offset(xPos, weightYPos)
                )

                val weightText = if (weekData.isNotEmpty()) {
                    " ${"%.1f".format(averageWeight)}"
                } else {
                    ""
                }
                drawContext.canvas.nativeCanvas.drawText(
                    weightText,
                    xPos,
                    weightYPos - 40f,
                    Paint().apply {
                        color = Color.Black.toArgb()
                        textSize = 50f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
                if (datas.size > 1) {
                    val previousPoint = datas[datas.size - 2]
                    drawLine(
                        color = Color.Black,
                        start = previousPoint,
                        end = point,
                        strokeWidth = 5f
                    )
                }
            }
        }
    }
}

package jp.ac.it_college.std.s23018.recordingcalendar.ui.graph

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun WeekGraphScreen(
    navController: NavController,
    userInformation: UserEntity? = null
) {

    val locale = Locale.getDefault()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val startOfWeek = selectedDate.with(DayOfWeek.MONDAY)
    val endOfWeek = startOfWeek.plusDays(6)

    // ユーザー情報の体重情報を取得
    var userInfo by remember { mutableStateOf(userInformation) }

    // 記録画面に記録されている体重データ
    var weights by remember { mutableStateOf<List<WeightEntity>>(emptyList()) }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val user_db = app.container.userRepository
    val weight_db = app.container.recordRepository

    val coroutineScope = rememberCoroutineScope()

    fun refreshData() {
        coroutineScope.launch {
            val fetchedUser = withContext(Dispatchers.IO) { user_db.getUser() }
            userInfo = fetchedUser

            val startDate = startOfWeek.format(DateTimeFormatter.ISO_DATE)
            val endDate = endOfWeek.format(DateTimeFormatter.ISO_DATE)

            val weightData = withContext(Dispatchers.IO) { weight_db.getWeightOfWeek(startDate, endDate) }
            weights = weightData
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    LaunchedEffect(selectedDate) {
        refreshData()
    }

    // 週の日付表示
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { selectedDate = selectedDate.minusWeeks(1) }) {
            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Week")
        }

        Text(
            text = "${startOfWeek.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale))} 〜 " +
                    endOfWeek.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
        )

        IconButton(onClick = { selectedDate = selectedDate.plusWeeks(1) }) {
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Week")
        }
    }

    // 体重の軸
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            // 横軸（曜日）
            val xAxisStart = Offset(90f, size.height - 100f)
            val xAxisEnd = Offset(size.width - 40f, size.height - 100f)
            drawLine(color = Color.Black, start = xAxisStart, end = xAxisEnd, strokeWidth = 5f)

            // 週の日付リストを生成
            val dateRange = generateSequence(startOfWeek) { it.plusDays(1) }
                .takeWhile { !it.isAfter(endOfWeek) }
                .toList()

            // ロケールに応じた曜日リストを生成
            val daysOfWeek = dateRange.map { date ->
                date.format(DateTimeFormatter.ofPattern("E", locale))
            }

            val xStep = (size.width - 100) / daysOfWeek.size
            val offset = xStep / 2

            daysOfWeek.forEachIndexed { index, day ->
                val xPos = 90f + xStep * index + offset
                val textColor = when (index) {
                    5 -> Color.Blue
                    6 -> Color.Red
                    else -> Color.Black
                }

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        day,
                        xPos,
                        size.height - 20f,
                        Paint().apply {
                            color = textColor.toArgb()
                            textSize = 50f
                        }
                    )
                }
            }

            // 縦軸（体重）
            val yAxisStart = Offset(95f, 100f)
            val yAxisEnd = Offset(95f, size.height - 100f)
            drawLine(color = Color.Black, start = yAxisStart, end = yAxisEnd, strokeWidth = 5f)

            val weightMax = ((userInfo?.weight ?: 0f) + 2f).toFloat()
            val weightMin = ((userInfo?.weight ?: 0f) - 2f).toFloat()
            val weightRange = weightMax - weightMin
            val yStepHeight = (yAxisEnd.y - yAxisStart.y) / weightRange

            for (i in 0..(weightRange.toInt())) {
                val weightLabel = weightMin + i
                val yPos = yAxisEnd.y - (i * yStepHeight)

                drawContext.canvas.nativeCanvas.drawText(
                    "${weightLabel.toInt()}",
                    yAxisStart.x - 20f,
                    yPos,
                    Paint().apply {
                        color = if (i == weightRange.toInt() / 2) Color.Blue.toArgb() else Color.Black.toArgb()
                        textSize = 50f
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                )
            }

            // 体重データを曜日に合わせて表示
            val weightMap = weights.associateBy { LocalDate.parse(it.date) }
            val datas = mutableListOf<Offset>()

            dateRange.forEachIndexed { index, date ->
                val xPos = 90f + xStep * index + offset
                val weightEntity = weightMap[date]

                val weightAsFloat = weightEntity?.weight?.toFloat() ?: 0f

                val weightPos = when {
                    weightAsFloat < weightMin -> 0f
                    weightAsFloat > weightMax -> (weightMax - weightMin)
                    else -> (weightAsFloat - weightMin)
                }

                val weightYPos = if (weightEntity != null) {
                    yAxisEnd.y - (weightPos * yStepHeight)
                } else {
                    yAxisEnd.y
                }

                val point = Offset(xPos, weightYPos)
                datas.add(point)

                drawCircle(
                    color = if (weightEntity != null) Color.Black else Color.Gray,
                    radius = 20f,
                    center = Offset(xPos, weightYPos)
                )

                val weightText = if (weightEntity != null) {
                    " ${"%.1f".format(weightAsFloat)}"
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
                    drawLine(color = Color.Black, start = previousPoint, end = point, strokeWidth = 5f)
                }
            }
        }
    }
}
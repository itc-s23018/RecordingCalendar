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
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun YearGraphScreen(
    navController: NavController,
    userInformation: UserEntity? = null
) {
    var selectedYear by remember { mutableStateOf(LocalDate.now()) }

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
            val fetchedUser = user_db.getUser()
            userInfo = fetchedUser

            val year = selectedYear.year.toString()
            val fetchedWeight = weight_db.getWeightOfYear(year)
            weights = fetchedWeight
        }
    }

    LaunchedEffect(selectedYear) {
        refreshData()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            selectedYear = selectedYear.minusYears(1)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous Year"
            )
        }

        Text(
            text = "${selectedYear.year}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(onClick = {
            selectedYear = selectedYear.plusYears(1)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next Year"
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            // 横軸 (Months)
            val xAxisStart = Offset(90f, size.height - 100f)
            val xAxisEnd = Offset(size.width - 40f, size.height - 100f)
            drawLine(
                color = Color.Black,
                start = xAxisStart,
                end = xAxisEnd,
                strokeWidth = 5f
            )

            val xStep = (size.width - 90) / 12
            val offset = xStep / 2

            val months = (1..12).map { it.toString() }

            for (i in months.indices) {
                val xPos = 90f + xStep * i + offset

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        months[i],
                        xPos,
                        size.height - 20f,
                        Paint().apply {
                            textSize = 50f
                        }
                    )
                }
            }

            // 縦軸 (Weight)
            val yAxisStart = Offset(95f, 100f)
            val yAxisEnd = Offset(95f, size.height - 100f)
            drawLine(
                color = Color.Black,
                start = yAxisStart,
                end = yAxisEnd,
                strokeWidth = 5f
            )

            val userWeight = (userInfo?.weight ?: 0f)
            val weightMax = userWeight + 2f
            val weightMin =  userWeight - 2f
            val weightRange = weightMax - weightMin

            val yStepHeight = (yAxisEnd.y - yAxisStart.y) / weightRange

            for (i in 0..(weightRange.toInt())) {
                val weightLabel = weightMin + i
                val yPos = yAxisEnd.y - (i * yStepHeight)

                drawContext.canvas.nativeCanvas.drawText(
                    "${weightLabel.toInt()}",
                    yAxisStart.x - 20f,
                    yPos,
                    android.graphics.Paint().apply {
                        color = if (i == weightRange.toInt() / 2) Color.Blue.toArgb() else Color.Black.toArgb()
                        textSize = 50f
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                )
            }

            //記録を表示
            val datas = mutableListOf<Offset>()

            months.forEachIndexed { index, month ->
                val xPos = 90f + xStep * index + offset

               val monthWeights = weights.filter { weight ->
                   val weightDate = LocalDate.parse(weight.date, DateTimeFormatter.ISO_DATE)
                   weightDate.monthValue == month.toInt()
               }

               val averageWeight = if (monthWeights.isNotEmpty()) {
                   monthWeights.map { it.weight }.average().toFloat()
               } else {
                   weightMin
               }

                val weightPos = when {
                    averageWeight < weightMin -> 0.09f
                    averageWeight > weightMax -> (weightMax - weightMin)
                    else -> (averageWeight - weightMin)
                }

                val weightYPos = yAxisEnd.y - (weightPos * weightMin)

                val point = Offset(xPos, weightYPos)
                datas.add(point)

                drawCircle(
                    color = if (!monthWeights.isNotEmpty()) Color.Gray else Color.Black,
                    radius = 20f,
                    center = Offset(xPos, weightYPos)
                )

                val weightText = if (monthWeights.isNotEmpty()) {
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

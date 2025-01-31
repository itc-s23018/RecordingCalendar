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
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun MonthGraphScreen(
    navController: NavController,
    userInformation: UserEntity? = null
) {
    var currentMonthAndYear by remember { mutableStateOf(LocalDate.now()) }

    // ユーザー情報の体重
    var userInfo by remember {
        mutableStateOf(userInformation)
    }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val db = app.container.userRepository

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        coroutineScope.launch {
            val fetchedUser = db.getUser()
            userInfo = fetchedUser
        }
    }

    // 月の月曜日の日付を取得
    val mondaysInMonth = remember(currentMonthAndYear) {
        val firstDayofMonth = currentMonthAndYear.withDayOfMonth(1)
        val firstMonday = firstDayofMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
        generateSequence(firstMonday) { it.plusWeeks(1)  }
            .takeWhile { it.month == currentMonthAndYear.month }
            .toList()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            currentMonthAndYear = currentMonthAndYear.minusMonths(1)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous Month"
            )
        }

        Text(
            text = "${currentMonthAndYear.year}年 ${currentMonthAndYear.monthValue}月",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(onClick = {
            currentMonthAndYear = currentMonthAndYear.plusMonths(1)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next Month"
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            // 横軸（週の月曜日の日付）
            val xAxisStart = Offset(90f, size.height - 100f)
            val xAxisEnd = Offset(size.width - 40f, size.height - 100f)
            drawLine(
                color = Color.Black,
                start = xAxisStart,
                end = xAxisEnd,
                strokeWidth = 5f
            )

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

            // 縦軸（体重）
            val yAxisStart = Offset(95f, 100f)
            val yAxisEnd = Offset(95f, size.height - 100f)
            drawLine(
                color = Color.Black,
                start = yAxisStart,
                end = yAxisEnd,
                strokeWidth = 5f
            )

            // ユーザー体重±2の範囲を設定
            val weightMax = (userInfo?.weight ?: 0f) + 2f
            val weightMin = (userInfo?.weight ?: 0f) - 2f
            val weightRange = weightMax - weightMin

            // 体重の目盛りの間隔（y軸のステップ高さ）
            val yStepHeight = (yAxisEnd.y - yAxisStart.y) / weightRange

            // 体重ラベルを下から上に表示
            for (i in 0..(weightRange.toInt())) {
                val weightLabel = weightMin + i // 下から順に描画
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
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun MonthGraphScreenPreview() {
//    MonthGraphScreen()
//}
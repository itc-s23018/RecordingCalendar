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
import java.time.format.DateTimeFormatter

@Composable
fun WeekGraphScreen(
    navController: NavController,
    userInformation: UserEntity? = null
) {

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val startOfWeek = selectedDate.with(DayOfWeek.MONDAY)
    val endOfWeek = startOfWeek.plusDays(6)

    //ユーザー情報の体重情報を取得
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

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            selectedDate = selectedDate.minusWeeks(1)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous Week"
            )
        }

        Text(
            text = "${startOfWeek.format(DateTimeFormatter.ofPattern("M月d日"))}〜${endOfWeek.format(DateTimeFormatter.ofPattern("M月d日"))}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(onClick = {
            selectedDate = selectedDate.plusWeeks(1)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next Week"
            )
        }

    }

    //体重の軸
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            // 横軸（曜日）
            val xAxisStart = Offset(90f, size.height - 100f)
            val xAxisEnd = Offset(size.width - 40f, size.height - 100f)
            drawLine(
                color = Color.Black,
                start = xAxisStart,
                end = xAxisEnd,
                strokeWidth = 5f
            )

            val daysOfWeek = listOf("月", "火", "水", "木", "金", "土", "日")
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
package jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.it_college.std.s23018.recordingcalendar.R
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navigateRecordEntry: () -> Unit = {},
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // 現在の日付を取得
    val currentCalendar = Calendar.getInstance()
    val initialYear = currentCalendar.get(Calendar.YEAR)
    val initialMonth = currentCalendar.get(Calendar.MONTH) + 1

    var currentYear by remember { mutableStateOf(initialYear) }
    var currentMonth by remember { mutableStateOf(initialMonth) }

    // システムのロケールを取得
    val currentLocale = Locale.getDefault()
    val isEnglish = currentLocale.language == "en"

    // 曜日のリストを取得
    val weekDays = if (isEnglish) {
        // 英語の場合、曜日を3文字に設定
        DateFormatSymbols().shortWeekdays.filter { it.isNotEmpty() }
    } else {
        // 日本語の場合、曜日を1文字に設定
        listOf("日", "月", "火", "水", "木", "金", "土")
    }

    // 現在の月のカレンダーを取得
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, currentYear)
    calendar.set(Calendar.MONTH, currentMonth - 1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    // 月の最初の日の曜日を取得
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    // 月の日数を取得
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    Scaffold(
        topBar = { //トップバー
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.calendar),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center) // 中央配置
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // 現在の年月
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    if (currentMonth == 1) {
                        currentMonth = 12
                        currentYear -= 1
                    } else {
                        currentMonth -= 1
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "前の月"
                    )
                }
                Text(
                    text = "$currentYear 年 $currentMonth 月",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                IconButton(onClick = {
                    if (currentMonth == 12) {
                        currentMonth = 1
                        currentYear += 1
                    } else {
                        currentMonth += 1
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "次の月"
                    )
                }
            }

            // 曜日表示
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEachIndexed { index, day ->
                    val textColor = when (index) {
                        0 -> Color.Red   // 日曜日 (index 0)
                        6 -> Color.Blue  // 土曜日 (index 6)
                        else -> Color.Black
                    }
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        ),
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f), // 各曜日を均等に配置
                        textAlign = TextAlign.Center // 中央揃え
                    )
                }
            }

            // 日付の表示
            Spacer(modifier = Modifier.height(8.dp))
            val daysInWeek = 7
            val emptySlots = firstDayOfWeek - 1 // 最初の日曜日まで空のスペース

            Column(
                modifier = Modifier.fillMaxHeight(0.6f)
            ) {
                for (week in 0..5) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (day in 1..daysInWeek) {
                            val currentDay = week * daysInWeek + (day - emptySlots)
                            if (currentDay in 1..daysInMonth) {
                                val textColor = when {
                                    (day == 1) -> Color.Red
                                    (day == 7) -> Color.Blue
                                    else -> Color.Black
                                }
                                Text(
                                    text = currentDay.toString(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 23.sp,
                                        color = textColor
                                    ),
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .width(40.dp)
                                        .weight(1f),
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    if (week * daysInWeek + emptySlots < daysInMonth) {
                        Spacer(modifier = Modifier.height(25.dp))
                    }

                    if (week * daysInWeek + emptySlots >= daysInMonth) {
                        break
                    }
                }
            }


            // 記録画面に遷移するボタン
            Button(
                onClick = {
                    navController.navigate("record")
                },
                modifier = Modifier
                    .fillMaxWidth() // ボタンの幅を画面幅に合わせる
                    .padding(16.dp)
            ) {
                Text(text = "記録画面を開くテスト", fontSize = 16.sp)
            }
        }
    }
}



@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen(
        navController = rememberNavController()
    )
}
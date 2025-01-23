package jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.app.NotificationCompat.Style
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.it_college.std.s23018.recordingcalendar.R
import jp.ac.it_college.std.s23018.recordingcalendar.RecordingCalendarApplication
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.text.DateFormatSymbols
import java.time.LocalDate
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
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH) // 今日の日付

    var currentYear by remember { mutableStateOf(initialYear) }
    var currentMonth by remember { mutableStateOf(initialMonth) }

    // システムのロケールを取得
    val currentLocale = Locale.getDefault()
    val isEnglish = currentLocale.language == "en"

    // 曜日のリストを取得
    val weekDays = if (isEnglish) {
        DateFormatSymbols().shortWeekdays.filter { it.isNotEmpty() }
    } else {
        listOf("日", "月", "火", "水", "木", "金", "土")
    }

    // 現在の月のカレンダーを取得
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, currentYear)
    calendar.set(Calendar.MONTH, currentMonth - 1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    // 月の最初の曜日を取得
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    // 月の日数を取得
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    //簡単な記録の表示するためデータを取得

    val weightRecords = remember { mutableStateOf<Map<LocalDate, WeightEntity>>(emptyMap()) }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val db = app.container.recordRepository

    LaunchedEffect(currentYear, currentMonth) {
        val startDate = LocalDate.of(currentYear, currentMonth, 1)
        val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())

        val records = withContext(Dispatchers.IO) {
            db.getWeightsByMonth(startDate.toString(), endDate.toString())
        }.associateBy { LocalDate.parse(it.date) }

        weightRecords.value = records
    }





    Scaffold(
        topBar = { //トップバー
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.calendar),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
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
                        contentDescription = "Previous Month"
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
                        contentDescription = "Next Month"
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
                        0 -> Color.Red
                        6 -> Color.Blue
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
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 日付の表示
            Spacer(modifier = Modifier.height(6.dp))
            val daysInWeek = 7
            val emptySlots = firstDayOfWeek - 1

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                for (week in 0..5) {
                    if( week > 0){
                        Divider(
                            color = Color.Black,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 0.5.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (day in 1..daysInWeek) {
                            val currentDayInCell = week * daysInWeek + (day - emptySlots)
                            if (currentDayInCell in 1..daysInMonth) {
                                val isToday = currentYear == initialYear && currentMonth == initialMonth && currentDayInCell == currentDay
                                val textColor = when {
                                    (day == 1) -> Color.Red
                                    (day == 7) -> Color.Blue
                                    else -> Color.Black
                                }

                                Card(
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(50.dp)
                                        .clickable { navController.navigate("record/$currentYear/$currentMonth/$currentDayInCell") }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0xFFF8F8FF))
                                    ) {

                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .size(35.dp)
                                                .padding(start = 5.dp, top = 8.dp)
                                                .background(
                                                    color = if (isToday) Color.Red else Color.Transparent,
                                                    shape = CircleShape
                                                )
                                        ) {
                                            Text(
                                                text = currentDayInCell.toString(),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 20.sp,
                                                    color = if (isToday) Color.White else textColor
                                                ),
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(5.dp))

                                        val recordForDay = weightRecords.value[LocalDate.of(currentYear, currentMonth, currentDayInCell)]

                                       if(recordForDay != null){
                                           Text(
                                               text = "${recordForDay.weight}Kg",
                                               style = MaterialTheme.typography.bodySmall.copy(
                                                   fontSize = 12.sp
                                               ),
                                               modifier = Modifier
                                                   .align(Alignment.BottomCenter)
                                                   .padding(bottom = 8.dp)
                                           )
                                       } else {
                                           Text(
                                               text = "記録なし",
                                               style = MaterialTheme.typography.bodySmall.copy(
                                                   fontSize = 12.sp
                                               ),
                                               modifier = Modifier
                                                   .align(Alignment.BottomCenter)
                                                   .padding(bottom = 8.dp)
                                           )
                                       }



                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    if (week * daysInWeek + emptySlots < daysInMonth) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    if (week * daysInWeek + emptySlots >= daysInMonth) {
                        break
                    }
                }
            }
        }
    }
}




@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen(
        navController = rememberNavController(),

    )
}
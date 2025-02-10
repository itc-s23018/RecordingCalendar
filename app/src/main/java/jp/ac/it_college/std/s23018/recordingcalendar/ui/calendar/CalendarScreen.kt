package jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsGolf
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.it_college.std.s23018.recordingcalendar.R
import jp.ac.it_college.std.s23018.recordingcalendar.RecordingCalendarApplication
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog.InputUserDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarScreen(
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

    val formattedDate = if (currentLocale.language == Locale.JAPANESE.language) {
        // 日本語の場合
        DateTimeFormatter.ofPattern("yyyy年 M月").withLocale(currentLocale).format(LocalDate.of(currentYear, currentMonth, 1))
    } else {
        // 英語などその他のロケールの場合
        DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(currentLocale).format(LocalDate.of(currentYear, currentMonth, 1))
    }

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

    val motionsRecords = remember { mutableStateOf<Map<LocalDate, List<MotionEntity>>>(emptyMap()) }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val record_db = app.container.recordRepository

    LaunchedEffect(currentYear, currentMonth) {
        val startDate = LocalDate.of(currentYear, currentMonth, 1)
        val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())

        val weightrecords = withContext(Dispatchers.IO) {
            record_db.getWeightsByMonth(startDate.toString(), endDate.toString())
        }.associateBy { LocalDate.parse(it.date) }

        val motionsrecords = withContext(Dispatchers.IO) {
            record_db.getMotionsByMonth(startDate.toString(), endDate.toString())
        }.groupBy { LocalDate.parse(it.date) }

        weightRecords.value = weightrecords
        motionsRecords.value = motionsrecords
    }

    //運動名をアイコンで表示させる
    val motionsIcons = mapOf(
        "walking" to Icons.Default.DirectionsWalk,
        "running" to Icons.Default.DirectionsRun,
        "cycling" to Icons.Default.DirectionsBike,
        "yoga" to Icons.Default.SelfImprovement,
        "muscle_training" to Icons.Default.FitnessCenter,
        "swimming" to Icons.Default.Pool,
        "baseball" to Icons.Default.SportsBaseball,
        "basketball" to Icons.Default.SportsBasketball,
        "soccer" to Icons.Default.SportsSoccer,
        "golf" to Icons.Default.SportsGolf
    )

    //ユーザー登録
    var userInformation by remember { mutableStateOf<UserEntity?>(null) }
    var showUserInputDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val user_db = app.container.userRepository
    val message = context.getString(R.string.input_user)

    fun handleUserConfirm(name: String, weight: Float, targetWeight: Float) {
        coroutineScope.launch {
            user_db.insertUser(
                UserEntity(
                    name = name,
                    weight = weight,
                    targetWeight = targetWeight
                )
            )
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            showUserInputDialog = false
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val user = user_db.getUser()
            if (user != null) {
                userInformation = user
            } else {
                showUserInputDialog = true
            }
        }
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

        if(showUserInputDialog) {
            InputUserDialog(onConfirm = {name, weight, targetWeight ->
                handleUserConfirm(name = name, weight = weight, targetWeight = targetWeight)
            },
                onDismiss = {showUserInputDialog = false}
            )
        }


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
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                            fontSize = 18.sp,
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
                                        .width(55.dp)
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
                                                .size(30.dp)
                                                .padding(start = 5.dp, top = 8.dp)
                                                .background(
                                                    color = if (isToday) Color.Red else Color.Transparent,
                                                    shape = CircleShape,
                                                )
                                        ) {
                                            Text(
                                                text = currentDayInCell.toString(),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 18.sp,
                                                    color = if (isToday) Color.White else textColor
                                                ),
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }

                                        val weightRecordForDay = weightRecords.value[LocalDate.of(currentYear,currentMonth,currentDayInCell)]
                                        val motionRecordsForDay = motionsRecords.value[LocalDate.of(currentYear,currentMonth,currentDayInCell)]?.firstOrNull()

                                        Column(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .padding(bottom = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            if (weightRecordForDay != null) {

                                                Text(
                                                    text = "${weightRecordForDay.weight}Kg",
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontSize = 12.sp,
                                                        color = Color.White
                                                    ),
                                                    modifier = Modifier
                                                        .background(Color(0xFF4169e1), shape = RoundedCornerShape(3.dp))
                                                        .padding(4.dp)
                                                )
                                            }


                                            if (motionRecordsForDay != null) {
                                                Row(
                                                    modifier = Modifier
                                                        .padding(top = 5.dp)
                                                        .background(Color(0xFF00ff7f), shape = RoundedCornerShape(3.dp))
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(18.dp)
                                                            .background(Color.White, shape = CircleShape),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        val motionIcon =
                                                            motionsIcons[motionRecordsForDay.name]
                                                        if (motionIcon != null) {
                                                            Icon(
                                                                imageVector = motionIcon,
                                                                contentDescription = motionRecordsForDay.name,
                                                                modifier = Modifier.size((15.dp))
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        text = "${motionRecordsForDay.time} " + stringResource(id= R.string.time),
                                                        style = MaterialTheme.typography.bodySmall.copy(
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }
                                            }
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
        navController = rememberNavController()
        )
}
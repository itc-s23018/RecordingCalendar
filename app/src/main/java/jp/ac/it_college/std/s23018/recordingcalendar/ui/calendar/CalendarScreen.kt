package jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    val initialMouth = currentCalendar.get(Calendar.MONTH) + 1

    var currentYear by remember { mutableStateOf(initialYear)}
    var currentMonth by remember { mutableStateOf(initialMouth) }

    val currentLocal = Locale.getDefault()
    val isEnglish = currentLocal.language == "en"

    val weekDays = if(isEnglish){
       DateFormatSymbols().shortWeekdays.filter { it.isNotEmpty() }
    } else {
        listOf("日","月","火","水","木","金","土")
    }


    Scaffold(
        topBar = {
            // トップバー内でタイトルを中央配置
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
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (currentMonth == 1) {
                        currentMonth = 12
                        currentYear -= 1
                    } else {
                        currentMonth -= 1
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Future Month"
                    )
                }
                Text(
                    text = "$currentYear 年 $currentMonth 月",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                    )
                Button(onClick = {
                    if(currentMonth == 12) {
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
                            .weight(1f) // 曜日を均等に配置
                            .padding(4.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }


            Button(
                onClick = {
                    navController.navigate("record")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "記録画面を開くテスト")
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
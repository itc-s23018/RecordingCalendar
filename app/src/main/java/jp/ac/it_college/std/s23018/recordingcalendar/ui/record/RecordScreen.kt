package jp.ac.it_college.std.s23018.recordingcalendar.ui.record

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.it_college.std.s23018.recordingcalendar.R
import jp.ac.it_college.std.s23018.recordingcalendar.RecordingCalendarApplication
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import jp.ac.it_college.std.s23018.recordingcalendar.ui.RecordingCalendarAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    navController: NavController,
    selectedYear: String,
    selectedMonth: String,
    selectedDay: String,
    modifier: Modifier = Modifier
) {

    var selectedDate by remember {
        mutableStateOf(
            LocalDate.of(
                selectedYear.toInt(),
                selectedMonth.toInt(),
                selectedDay.toInt()
            )
        )
    }

    var weightRecord by remember {
        mutableStateOf<WeightEntity?>(null)
    }

    var motionRecord by remember {
        mutableStateOf<MotionEntity?>(null)
    }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val db = app.container.recordRepository

    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(selectedDate) {
        val weightData = withContext(Dispatchers.IO){
            db.getWeightByDate(selectedDate.toString())
        }

        val motionData = withContext(Dispatchers.IO){
            db.getMotionByDate(selectedDate.toString())
        }
        weightRecord = weightData
        motionRecord = motionData
    }

    Scaffold(
        topBar = {
            RecordingCalendarAppBar(
                title = stringResource(id = R.string.record),
                canNavigationBack = true,
                navigateUp = { navController.navigate("calendar") }
            )
        },
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        selectedDate = selectedDate.minusDays(1)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous Date"
                        )
                    }

                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    IconButton(onClick = {
                        selectedDate = selectedDate.plusDays(1)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Date"
                        )
                    }
                }

                weightRecord?.let {
                    Text(
                        text = "体重: ${it.weight}kg",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                } ?: run {
                    Text(
                        text = "体重: 記録なし",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            db.insertWeight(
                                WeightEntity(
                                    date = selectedDate.toString(),
                                    weight = 64.0f
                                )
                            )
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "体重を登録")
                }

                motionRecord?.let {
                    Text(
                        text = "運動記録:${it.motion} ${it.time}分",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                } ?: run {
                    Text(
                        text = "運動記録: なし",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        // ボタンクリック時に運動記録を登録
                        coroutineScope.launch {
                            db.insertMotion(
                                MotionEntity(
                                    date = selectedDate.toString(),
                                    motion = "Strength training",
                                    time = 45
                                )
                            )
                        }
                    }
                ) {
                    Text(text = "運動記録を登録")
                }
            }
        }
    )
}


@Composable
@Preview(showBackground = true)
private fun RecordScreenPreview() {
    RecordScreen(
        navController = rememberNavController(),
        selectedYear = "2024",
        selectedMonth = "12",
        selectedDay = "18"
    )
}
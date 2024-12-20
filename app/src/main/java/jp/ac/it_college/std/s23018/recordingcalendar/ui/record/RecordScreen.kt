package jp.ac.it_college.std.s23018.recordingcalendar.ui.record

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
        mutableStateOf<List<MotionEntity?>>(emptyList())
    }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val db = app.container.recordRepository

    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(selectedDate) {
        val weightData = withContext(Dispatchers.IO) {
            db.getWeightByDate(selectedDate.toString())
        }

        val motionData = withContext(Dispatchers.IO) {
            db.getMotionsByDate(selectedDate.toString())
        }
        weightRecord = weightData
        motionRecord = motionData ?: emptyList()
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
                    modifier = modifier
                        .fillMaxWidth(),
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "体重:",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${it.weight}kg",
                                fontSize = 30.sp,
                                modifier = Modifier.padding(end = 10.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.End
                        ) {

                            Text(
                                text = "編集する",
                                fontSize = 15.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            db.updateWeight(
                                                WeightEntity(
                                                    date = selectedDate.toString(),
                                                    weight = 64.3f
                                                )
                                            )
                                        }
                                    }
                            )
                        }
                    }
                    Divider(
                        color = Color.Gray,
                        thickness = 5.dp
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
                    Text(
                        text = "登録する",
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    db.insertWeight(
                                        WeightEntity(
                                            date = selectedDate.toString(),
                                            weight = 64.3f
                                        )
                                    )
                                }
                            }
                    )
                }

                if (motionRecord.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        motionRecord.forEach { motion ->
                            motion?.let {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                ) {
                                    Text(
                                        "運動記録:",
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        """
                                            ${motion.name},
                                            ${motion.time}分
                                        """.trimIndent(),
                                        fontSize = 30.sp,
                                        modifier = Modifier.padding(end = 10.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "編集する",
                                        fontSize = 15.sp,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .padding(start = 5.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    db.updateMotion(
                                                        motion.copy(name = "Swimming", time = 45)
                                                    )
                                                }
                                            }
                                    )
                                }
                                Divider(color = Color.Gray, thickness = 5.dp)

                                Text(
                                    text = "追加する",
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .clickable {
                                            coroutineScope.launch {
                                                db.insertMotion(
                                                    MotionEntity(
                                                        date = selectedDate.toString(),
                                                        name = "baseball",
                                                        time = 50
                                                    )
                                                )
                                            }
                                        }
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "運動記録: なし",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "記録する",
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    db.insertMotion(
                                        MotionEntity(
                                            date = selectedDate.toString(),
                                            name = "running",
                                            time = 45
                                        )
                                    )
                                }
                            }
                    )

                }
            }

        }
    )
}


//@Composable
//@Preview(showBackground = true)
//private fun RecordScreenPreview() {
//    RecordScreen(
//        navController = rememberNavController(),
//        selectedYear = "2024",
//        selectedMonth = "12",
//        selectedDay = "18"
//    )
//}
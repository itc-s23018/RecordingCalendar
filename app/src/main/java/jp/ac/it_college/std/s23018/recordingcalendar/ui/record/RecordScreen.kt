package jp.ac.it_college.std.s23018.recordingcalendar.ui.record

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.it_college.std.s23018.recordingcalendar.RecordingCalendarApplication
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import jp.ac.it_college.std.s23018.recordingcalendar.ui.RecordingCalendarAppBar
import kotlinx.coroutines.launch

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
    Scaffold(
        topBar = {
            RecordingCalendarAppBar(
                title = "記録画面",
                canNavigationBack = true,
                navigateUp = { navController.navigateUp() }
            )
        },
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "記録画面")
                Text(text = "$selectedYear 年 $selectedMonth 月 $selectedDay 日")

                // ボタンを追加して、データベースへのデータ挿入を試す
                val coroutineScope = rememberCoroutineScope()
                val app = navController.context.applicationContext as RecordingCalendarApplication
                val db = app.container.recordRepository
                val weight = WeightEntity("2024-12-13", 64.5f)
                val motion = MotionEntity("2024-12-13", "Running", 30)

                Button(
                    onClick = {
                        coroutineScope.launch {
                            // WeightEntity と MotionEntity をデータベースに挿入
                            db.insertWeight(weight)
                        }
                    }
                ) {
                    Text(text = "体重を登録")
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            db.insertMotion(motion)
                        }
                    }
                ) {
                    Text(text = "運動記録を登録")
                }
            }
        }
    )
}


//@Preview(showBackground = true)
//@Composable
//private fun RecordScreenPreview(){
//    RecordScreen(rememberNavController())
//}
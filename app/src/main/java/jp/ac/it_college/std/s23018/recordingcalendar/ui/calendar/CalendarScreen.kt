package jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onRecordClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "カレンダー")
        Button(
            onClick = onRecordClick
        ) {
            Text(text = "記録画面へ")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarScreenPreview(){
    CalendarScreen {  }
}


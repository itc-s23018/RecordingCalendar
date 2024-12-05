package jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onViewRecordClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "カレンダー")
        Button(onClick = onViewRecordClick) {
            Text("Go to Record Screen")
        }
    }
}


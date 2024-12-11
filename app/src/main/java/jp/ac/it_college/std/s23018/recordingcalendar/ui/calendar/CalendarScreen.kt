package jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import jp.ac.it_college.std.s23018.recordingcalendar.R
import jp.ac.it_college.std.s23018.recordingcalendar.ui.RecordingCalendarAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navigateRecordEntry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            RecordingCalendarAppBar(
                title = stringResource(R.string.calendar),
                canNavigationBack = false,
            )
        }
    ) { innerPading ->
        Button(
            onClick = {
                navigateRecordEntry
            }, modifier = Modifier.padding(innerPading)
        ) {
            Text(text = "記録画面を開くテスト")
        }
    }

}

@Preview
@Composable
private fun CalendarScrennPreview() {
    CalendarScreen()
}
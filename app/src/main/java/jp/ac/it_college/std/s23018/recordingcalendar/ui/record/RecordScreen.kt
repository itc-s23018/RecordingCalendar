package jp.ac.it_college.std.s23018.recordingcalendar.ui.record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(text = "記録画面")
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordScreenPreview(){
    RecordScreen()
}
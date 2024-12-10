package jp.ac.it_college.std.s23018.recordingcalendar.ui.graph

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GraphScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
    ) {
        Text("グラフ画面" )
    }
}

@Preview(showBackground = true)
@Composable
private fun GraphScreenPreview(){
    GraphScreen()
}
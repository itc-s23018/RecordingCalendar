package jp.ac.it_college.std.s23018.recordingcalendar.ui.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun YearGraphScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Year Graph")
    }
}

@Preview(showBackground = true)
@Composable
private fun YearGraphScreenPreview() {
    YearGraphScreen()
}
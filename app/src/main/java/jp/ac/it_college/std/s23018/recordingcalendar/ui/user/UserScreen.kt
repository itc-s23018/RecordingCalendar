package jp.ac.it_college.std.s23018.recordingcalendar.ui.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import jp.ac.it_college.std.s23018.recordingcalendar.R

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier.fillMaxSize()
    ) {
       Text(text = stringResource(id = R.string.user))
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScrrenPreview() {
    UserScreen()
}
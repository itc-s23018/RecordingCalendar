package jp.ac.it_college.std.s23018.recordingcalendar.ui.tab


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ac.it_college.std.s23018.recordingcalendar.R

@Composable
fun TabRowView(
    modifier: Modifier = Modifier,
    tabIndex: Int, onTabChange:(Int) -> Unit = {}
) {
    val tabs = listOf(
        stringResource(id = R.string.calendar),
        stringResource(id = R.string.graph),
        stringResource(id = R.string.user),
    )

    TabRow(
        modifier = modifier,
        selectedTabIndex = tabIndex,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[tabIndex])
                    .height(10.dp)
            )
        }
    ) {
        tabs.forEachIndexed { index, label ->
            Tab(
                selected = tabIndex == index,
                onClick = { onTabChange(index) },
                text = { Text(text = label) },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun TabRowViewPreview() {
    var index by remember { mutableStateOf(0) }
    TabRowView(tabIndex = index, onTabChange = {index = it})
}
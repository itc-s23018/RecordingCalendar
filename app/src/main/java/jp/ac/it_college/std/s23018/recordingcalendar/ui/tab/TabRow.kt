package jp.ac.it_college.std.s23018.recordingcalendar.ui.tab


import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.sp
import jp.ac.it_college.std.s23018.recordingcalendar.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRowView(
    modifier: Modifier = Modifier, tabIndex: Int, onTabChange: (Int) -> Unit = {}
) {
    val tabs = listOf(
        stringResource(id = R.string.calendar) to Icons.Default.CalendarMonth,
        stringResource(id = R.string.graph) to Icons.Default.ShowChart,
        stringResource(id = R.string.user) to Icons.Default.Person,
    )

    TabRow(modifier = modifier, selectedTabIndex = tabIndex) {
        tabs.forEachIndexed { index, (label, icon) ->
            Tab(
                selected = tabIndex == index,
                onClick = { onTabChange(index) },
                text = { Text(text = label,
                    fontSize = 18.sp) },
                icon = {
                   Icon(
                      imageVector = icon,
                       contentDescription = label,
                       modifier = Modifier.size(30.dp)
                   )
                }
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
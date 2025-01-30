package jp.ac.it_college.std.s23018.recordingcalendar.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import jp.ac.it_college.std.s23018.recordingcalendar.R
import jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar.CalendarScreen
import jp.ac.it_college.std.s23018.recordingcalendar.ui.graph.GraphScreen
import jp.ac.it_college.std.s23018.recordingcalendar.ui.user.UserScreen
import org.w3c.dom.Text as Text1

@Composable
fun TabRowScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
){
    var tabIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(100.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                TabRowView(tabIndex = tabIndex, onTabChange = { tabIndex = it })
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 選択されたタブに応じた画面を表示
            when (tabIndex) {
                0 -> CalendarScreen(
                    navController = navController
                )// カレンダー画面
                1 -> GraphScreen(
                    navController = navController
                ) // グラフ画面
                2 -> UserScreen(
                    navController = navController
                ) // ユーザー画面
            }
        }
    }
}


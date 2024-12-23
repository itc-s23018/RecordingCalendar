package jp.ac.it_college.std.s23018.recordingcalendar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.ac.it_college.std.s23018.recordingcalendar.ui.calendar.CalendarScreen
import jp.ac.it_college.std.s23018.recordingcalendar.ui.record.RecordScreen
import jp.ac.it_college.std.s23018.recordingcalendar.ui.tab.TabRowScreen

@Composable
fun AppNavigation(
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "tabRow"
    ) {
        composable("tabRow"){
            TabRowScreen(
                navController = navController
            )
        }
        composable("calendar") {
            CalendarScreen(
                navController = navController
            )
        }
        composable("record/{selectedYear}/{selectedMonth}/{selectedDay}") { backStackEntry ->
            val selectedYear = backStackEntry.arguments?.getString("selectedYear")
            val selectedMonth = backStackEntry.arguments?.getString("selectedMonth")
            val selectedDay = backStackEntry.arguments?.getString("selectedDay")
            RecordScreen(
                navController = navController,
                selectedYear = selectedYear ?: "未選択",
                selectedMonth = selectedMonth ?: "未選択",
                selectedDay = selectedDay ?: "未選択",
            )
        }
    }
}
package jp.ac.it_college.std.s23018.recordingcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import jp.ac.it_college.std.s23018.recordingcalendar.ui.navigation.AppNavigation
import jp.ac.it_college.std.s23018.recordingcalendar.ui.tab.TabRowScreen
import jp.ac.it_college.std.s23018.recordingcalendar.ui.theme.RecordingCalendarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           RecordingCalendarTheme {
               AppNavigation()
               TabRowScreen()
           }
        }
    }
}


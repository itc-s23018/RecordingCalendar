package jp.ac.it_college.std.s23018.recordingcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import jp.ac.it_college.std.s23018.recordingcalendar.ui.theme.RecordingCalendarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordingCalendarTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    
                }
            }
        }
    }
}


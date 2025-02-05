package jp.ac.it_college.std.s23018.recordingcalendar

import android.content.Intent
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import jp.ac.it_college.std.s23018.recordingcalendar.ui.navigation.AppNavigation
import jp.ac.it_college.std.s23018.recordingcalendar.ui.theme.RecordingCalendarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ACTIVITY_RECOGNITION の権限をリクエスト
        requestPermissions(
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1
        )
        setContent {
            RecordingCalendarTheme {
                AppNavigation()

                LaunchedEffect(Unit) {
                    startStepCounterService()
                }
            }
        }
    }

    private fun startStepCounterService() {
        val intent = Intent(this, StepCounterService::class.java)
        startForegroundService(intent)
    }
}

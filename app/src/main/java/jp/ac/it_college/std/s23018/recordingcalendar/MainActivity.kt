package jp.ac.it_college.std.s23018.recordingcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import jp.ac.it_college.std.s23018.recordingcalendar.ui.navigation.AppNavigation
import jp.ac.it_college.std.s23018.recordingcalendar.ui.theme.RecordingCalendarTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           RecordingCalendarTheme {
               AppNavigation()

               Surface {
                   val coroutineScope = rememberCoroutineScope()
                   val app = application as RecordingCalendarApplication
                   val db = app.container.userRepository
                   val user = UserEntity(
                       name = "山田　太郎", weight = 65.0f, targetWeight = 60.0f
                   )
                   Row {
                       Button(
                           onClick = {
                               coroutineScope.launch {
                                   db.insertUser(user)
                               }
                           }) {
                           Text(text = "ユーザー登録テスト")
                       }
                   }
               }
           }
        }
    }
}


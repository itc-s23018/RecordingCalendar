package jp.ac.it_college.std.s23018.recordingcalendar.data

import android.content.Context
import androidx.room.Database
import jp.ac.it_college.std.s23018.recordingcalendar.data.impl.UserRepositoryImpl
import jp.ac.it_college.std.s23018.recordingcalendar.data.repository.UserRepository

interface AppContainer {
    val userRepository: UserRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(RecordingCalendarDatabase.getDatabase(context).userDao())
    }
}
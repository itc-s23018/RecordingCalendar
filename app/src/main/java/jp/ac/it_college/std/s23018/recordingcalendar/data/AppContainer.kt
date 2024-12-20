package jp.ac.it_college.std.s23018.recordingcalendar.data

import android.content.Context
import jp.ac.it_college.std.s23018.recordingcalendar.data.repository.impl.RecordRepositoryImpl
import jp.ac.it_college.std.s23018.recordingcalendar.data.impl.UserRepositoryImpl
import jp.ac.it_college.std.s23018.recordingcalendar.data.repository.RecordRepository
import jp.ac.it_college.std.s23018.recordingcalendar.data.repository.UserRepository

interface AppContainer {
    val userRepository: UserRepository
    val recordRepository: RecordRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(RecordingCalendarDatabase.getDatabase(context).userDao())
    }

    override val recordRepository: RecordRepository by lazy {
        RecordRepositoryImpl(RecordingCalendarDatabase.getDatabase(context).recordDao())
    }
}
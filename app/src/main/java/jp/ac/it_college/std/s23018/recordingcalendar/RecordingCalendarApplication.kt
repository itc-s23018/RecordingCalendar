package jp.ac.it_college.std.s23018.recordingcalendar

import android.app.Application
import jp.ac.it_college.std.s23018.recordingcalendar.data.AppContainer
import jp.ac.it_college.std.s23018.recordingcalendar.data.AppDataContainer

class RecordingCalendarApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
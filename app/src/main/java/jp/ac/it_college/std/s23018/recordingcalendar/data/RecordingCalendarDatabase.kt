package jp.ac.it_college.std.s23018.recordingcalendar.data

import android.content.Context
import androidx.room.*
import jp.ac.it_college.std.s23018.recordingcalendar.data.dao.RecordDao
import jp.ac.it_college.std.s23018.recordingcalendar.data.dao.UserDao
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.StepEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity

@Database(
    entities = [UserEntity::class, WeightEntity::class, MotionEntity::class, StepEntity::class],
    version = 3,
    exportSchema = true
)
abstract class RecordingCalendarDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: RecordingCalendarDatabase? = null

        fun getDatabase(context: Context): RecordingCalendarDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RecordingCalendarDatabase::class.java,
                    "recording_calendar_db"
                )

                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

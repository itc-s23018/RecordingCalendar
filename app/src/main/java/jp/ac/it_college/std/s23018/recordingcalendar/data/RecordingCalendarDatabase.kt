package jp.ac.it_college.std.s23018.recordingcalendar.data

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import jp.ac.it_college.std.s23018.recordingcalendar.data.dao.RecordDao
import jp.ac.it_college.std.s23018.recordingcalendar.data.dao.UserDao
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [UserEntity::class, WeightEntity::class, MotionEntity::class],
    version = 2,  // バージョン番号を変更
    exportSchema = false)
abstract class RecordingCalendarDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var Instance: RecordingCalendarDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): RecordingCalendarDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, RecordingCalendarDatabase::class.java,
                    "recordCalendar_database"
                )
                    .fallbackToDestructiveMigration() // データベースを再作成
                    .build().also { Instance = it }
            }
        }
    }
}

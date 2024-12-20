package jp.ac.it_college.std.s23018.recordingcalendar.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "motion")
data class MotionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String = getCurrentDate().substring(0,10),
    val name: String,
    val time: Int
)


fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(System.currentTimeMillis()))
}
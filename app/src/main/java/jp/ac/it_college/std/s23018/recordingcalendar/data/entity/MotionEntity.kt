package jp.ac.it_college.std.s23018.recordingcalendar.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "motion")
data class MotionEntity(
    @PrimaryKey val date: String,
    val motion: String,
    val time: Int
)
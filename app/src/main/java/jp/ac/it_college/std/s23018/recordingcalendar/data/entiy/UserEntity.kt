package jp.ac.it_college.std.s23018.recordingcalendar.data.entiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val weight: Float,
    val targetWeight: Float
)

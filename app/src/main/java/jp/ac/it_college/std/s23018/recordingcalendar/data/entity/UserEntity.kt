package jp.ac.it_college.std.s23018.recordingcalendar.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    indices = [Index(value = ["name"], unique = true)]
    )
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val weight: Float,
    val targetWeight: Float
)

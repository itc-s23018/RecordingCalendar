package jp.ac.it_college.std.s23018.recordingcalendar.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight")
data class Weight(
    @PrimaryKey val date: String,
    val weight: Float
)

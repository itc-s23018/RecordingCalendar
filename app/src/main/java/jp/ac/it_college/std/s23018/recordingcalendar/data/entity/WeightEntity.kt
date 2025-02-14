package jp.ac.it_college.std.s23018.recordingcalendar.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "weight")
data class WeightEntity(
    @PrimaryKey val date: String = getCurrentDate().substring(0,10),
    val weight: Float
)

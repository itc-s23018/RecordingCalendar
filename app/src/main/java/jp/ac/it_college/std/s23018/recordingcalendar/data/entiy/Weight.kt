package jp.ac.it_college.std.s23018.recordingcalendar.data.entiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "weight")
data class Weight(
    @PrimaryKey val date: String,
    val weight: Float
)

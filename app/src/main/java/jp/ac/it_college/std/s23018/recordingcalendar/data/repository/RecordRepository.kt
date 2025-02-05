package jp.ac.it_college.std.s23018.recordingcalendar.data.repository

import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity

interface RecordRepository {
    suspend fun insertWeight(weight:WeightEntity)
    suspend fun updateWeight(weight:WeightEntity)
    suspend fun getWeightByDate(date: String):WeightEntity?
    suspend fun getWeightsByMonth(startDate:String, endDate: String):List<WeightEntity>

    suspend fun insertMotion(motions: MotionEntity)
    suspend fun updateMotion(motion:MotionEntity)
    suspend fun deleteMotion(motion: MotionEntity)
    suspend fun getMotionsByDate(date: String): List<MotionEntity>
    suspend fun getMotionsByMonth(startDate: String, endDate: String):List<MotionEntity>

    suspend fun getWeightOfWeek(startDate: String, endDate: String):List<WeightEntity>
    suspend fun getWeightOfYear(year:String):List<WeightEntity>
}

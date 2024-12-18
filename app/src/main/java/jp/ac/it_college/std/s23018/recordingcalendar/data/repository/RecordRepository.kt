package jp.ac.it_college.std.s23018.recordingcalendar.data.repository

import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity

interface RecordRepository {
    suspend fun insertWeight(weight:WeightEntity)
    suspend fun insertMotion(motion:MotionEntity)

    suspend fun updateWeight(weight:WeightEntity)
    suspend fun updateMotion(motion:MotionEntity)

    suspend fun getWeightByDate(date: String):WeightEntity?
    suspend fun getMotionByDate(date: String): MotionEntity?

//    suspend fun getWeeklyWeights(weight: WeightEntity)
//    suspend fun getMonthlyWeights(weight: WeightEntity)
//    suspend fun getYearlyWeights(weight: WeightEntity)
}
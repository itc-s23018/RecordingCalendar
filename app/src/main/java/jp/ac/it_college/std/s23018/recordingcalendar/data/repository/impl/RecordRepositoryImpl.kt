package jp.ac.it_college.std.s23018.recordingcalendar.data.repository.impl

import jp.ac.it_college.std.s23018.recordingcalendar.data.dao.RecordDao
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.repository.RecordRepository
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val dao: RecordDao
): RecordRepository  {
    override suspend fun insertWeight(weight: WeightEntity) = dao.weightInsert(weight)
    override suspend fun updateWeight(weight: WeightEntity) = dao.weightUpdate(weight)
    override suspend fun getWeightByDate(date: String): WeightEntity? = dao.getWeightByDate(date)
    override suspend fun getWeightsByMonth(startDate: String, endDate: String): List<WeightEntity> = dao.getWeightsByMonth(startDate,endDate)

    override suspend fun insertMotion(motions: MotionEntity) = dao.motionInsert(listOf(motions))
    override suspend fun updateMotion(motion: MotionEntity) = dao.motionUpdate(motion)
    override suspend fun deleteMotion(motion: MotionEntity) = dao.motionDelete(motion)

    override suspend fun getMotionsByDate(date: String): List<MotionEntity>  = dao.getMotionsByDate(date)
    override suspend fun getMotionsByMonth(startDate: String, endDate: String): List<MotionEntity>  = dao.getMotionsByMonth(startDate, endDate)

    override suspend fun getWeightOfWeek(startDate: String, endDate: String): List<WeightEntity> = dao.getWeightsOfWeek(startDate,endDate)
}

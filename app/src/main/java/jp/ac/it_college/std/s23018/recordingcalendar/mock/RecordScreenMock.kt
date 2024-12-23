package jp.ac.it_college.std.s23018.recordingcalendar.mock

import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.repository.RecordRepository

object RecordScreenMock: RecordRepository {

    private val weightMockData = mutableListOf(
        WeightEntity("2024-12-23", 60.0f),
        WeightEntity("2024-12-22", 61.5f)
    )

    private val motionMockData = mutableListOf(
        MotionEntity(1,"2024-12-23","Running", 45),
        MotionEntity(1,"2024-12-23","Swimming", 30)
    )
    override suspend fun insertWeight(weight: WeightEntity) {
       weightMockData.add(weight)
    }

    override suspend fun updateWeight(weight: WeightEntity) {
     val index = weightMockData.indexOfFirst { it.date == weight.date }
        if(index != -1) {
            weightMockData[index] = weight
        }
    }

    override suspend fun getWeightByDate(date: String): WeightEntity? {
        return weightMockData.find { it.date == date }
    }

    override suspend fun insertMotion(motions: MotionEntity) {
        motionMockData.add(motions)
    }

    override suspend fun updateMotion(motion: MotionEntity) {
       val index = motionMockData.indexOfFirst { it.date == motion.date && it.name == motion.name }
        if(index != -1){
            motionMockData[index] == motion
        }
    }

    override suspend fun getMotionsByDate(date: String): List<MotionEntity> {
       return motionMockData.filter { it.date == date }
    }
}
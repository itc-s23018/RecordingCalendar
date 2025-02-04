
package jp.ac.it_college.std.s23018.recordingcalendar.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Insert //体重記録入力
    suspend fun weightInsert(weight: WeightEntity)

    @Update //体重記録編集
    suspend fun weightUpdate(weight: WeightEntity)

    @Query("SELECT * FROM weight WHERE date = :date") //体重記録表示(記録画面）
    fun getWeightByDate(date: String): WeightEntity?

    @Query("SELECT * FROM weight WHERE date BETWEEN :startDate AND :endDate") //体重記録表示(カレンダー画面)
    suspend fun getWeightsByMonth(startDate: String, endDate: String): List<WeightEntity>

    @Insert //運動記録入力・追加
    suspend fun motionInsert(motions:List<MotionEntity>)

    @Update //運動記録編集
    suspend fun motionUpdate(motion: MotionEntity)

    @Delete
    suspend fun motionDelete(motion: MotionEntity)

    @Query("SELECT * FROM motion WHERE date = :date") //運動記録表示(記録画面）
    fun getMotionsByDate(date: String): List<MotionEntity>

    @Query("SELECT * FROM motion WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getMotionsByMonth(startDate: String, endDate: String): List<MotionEntity>

    @Query("SELECT * FROM weight WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    suspend fun getWeightsOfWeek(startDate: String,endDate: String): List<WeightEntity>

}
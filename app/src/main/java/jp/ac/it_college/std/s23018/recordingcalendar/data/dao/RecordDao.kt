package jp.ac.it_college.std.s23018.recordingcalendar.data.dao

import androidx.room.Dao
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

    @Insert //運動記録入力・追加
    suspend fun motionInsert(motion:MotionEntity)

    @Update //体重記録編集
    suspend fun weightUpdate(weight: WeightEntity)

    @Update //運動記録編集
    suspend fun motionUpdate(motion: MotionEntity)

    @Query("SELECT * FROM weight WHERE date = :date") //体重記録表示(記録画面&カレンダー画面）
    fun getWeightByDate(date: String): WeightEntity?

    @Query("SELECT * FROM motion WHERE date = :date") //運動記録表示(記録画面&カレンダー画面）
    fun getMotionByDate(date: String): MotionEntity?

    @Query("SELECT * FROM weight WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")//１週間グラフの体重記録表示
    fun getWeeklyWeights(startDate: String, endDate: String): Flow<List<WeightEntity>>

    @Query("SELECT * FROM weight WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")//1ヶ月間グラフの体重記録表示
    fun getMonthlyWeights(startDate: String, endDate: String): Flow<List<WeightEntity>>

    @Query("SELECT * FROM weight WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")//１年間グラフの体重記録表示
    fun getYearlyWeights(startDate: String, endDate: String): Flow<List<WeightEntity>>


}
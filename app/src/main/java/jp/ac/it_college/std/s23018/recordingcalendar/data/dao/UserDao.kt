package jp.ac.it_college.std.s23018.recordingcalendar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert // ユーザー情報登録
    suspend fun insert(user:UserEntity)

    @Update //ユーザー情報編集
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1") //ユーザー情報表示（ユーザー画面）
    suspend fun getUser(): UserEntity
}
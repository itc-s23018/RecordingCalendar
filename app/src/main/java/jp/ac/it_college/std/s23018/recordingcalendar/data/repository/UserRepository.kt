package jp.ac.it_college.std.s23018.recordingcalendar.data.repository

import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    suspend fun insertUser(user:UserEntity)
    suspend fun updateUser(user:UserEntity)
    suspend fun getUser(): Flow<UserEntity>
}
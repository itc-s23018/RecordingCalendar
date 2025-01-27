package jp.ac.it_college.std.s23018.recordingcalendar.data.impl

import jp.ac.it_college.std.s23018.recordingcalendar.data.dao.UserDao
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao
): UserRepository {
    override suspend fun insertUser(user: UserEntity) = dao.insert(user)

    override suspend fun updateUser(user: UserEntity) = dao.update(user)

    override suspend fun getUser(): UserEntity  = dao.getUser()

}
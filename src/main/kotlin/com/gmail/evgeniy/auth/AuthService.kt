package com.gmail.evgeniy.auth

import com.gmail.evgeniy.backend.BackendServiceLocal
import com.gmail.evgeniy.entity.User
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random


object AuthService {

    private val users = CopyOnWriteArrayList<User>()
    private val tokenCodeCache: Cache<String, String>

    init {
        users.add(User("e87dc893-f6cf-478e-8d7b-30639ed70436", "111111", LocalDateTime.now().plusDays(1), true))
        users.add(User("7489fdcd-a056-4447-9a3a-c6199c89288c", "222222", LocalDateTime.now().plusDays(1), false))
        users.add(User("bb18583e-af29-4f9c-94c6-037a08a47ee9", "333333", LocalDateTime.now().minusDays(1), true))

        tokenCodeCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5))
                .build<String, String>()
    }

    private fun getUser(token: String): User {
        return users.first { token == it.token }

    }

    suspend fun isTokenExists(token: String): Boolean {
        return getUser(token).isActive
    }

    suspend fun isTokenAlive(token: String): Boolean {
        return LocalDateTime.now() <= getUser(token).tokenExpirationTime
    }

    suspend fun createToken(patientId: String): String {
        val user = users.firstOrNull { patientId == it.patientId }
        val newToken = UUID.randomUUID().toString()

        if (user == null) {
            users.add(User(patientId, newToken, LocalDateTime.now().plusDays(1), true))
        } else {
            user.token = newToken
        }
        return newToken
    }

    suspend fun isCorrectBirthday(token: String, date: LocalDate): Boolean {
        return BackendServiceLocal.load(getUser(token).patientId)?.birthday == date
    }

    suspend fun getPatientId(token: String): String {
        return getUser(token).patientId
    }

    suspend fun sendSmsForConfirmation(token: String) {
        val code = generateSequence { Random.nextInt(0, 9).toString() }
                .take(4).fold("", { acc, s -> acc + s })

        tokenCodeCache.put(token, code)
        //todo: sms send
        println("new code $code for token $token")
    }

    suspend fun checkPassword(token: String, code: String): Boolean {
        val isCorrect = tokenCodeCache.getIfPresent(token) == code
        if (isCorrect) {
            tokenCodeCache.invalidate(token)
        }
        return isCorrect
    }

    suspend fun updateToken(token: String) {
        getUser(token).tokenExpirationTime = LocalDateTime.now().plusDays(2)
    }


}
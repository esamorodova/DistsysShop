package ru.hse.cs.distsys.auth

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class AuthServiceImpl(val repository: UserEntityRepository) : AuthorizationService, AuthenticationService {
    private val secureRandom = SecureRandom()
    private val encoder = Base64.getUrlEncoder()

    private fun generateTokens(email: String): AuthorizationService.Tokens {
        val bytes = ByteArray(tokenLength)
        secureRandom.nextBytes(bytes)
        val accessToken = encoder.encode(bytes).toString() + ";" + email
        secureRandom.nextBytes(bytes)
        val refreshToken = encoder.encode(bytes).toString()
        return AuthorizationService.Tokens(accessToken, refreshToken)
    }

    @Transactional
    @Throws(Exception::class)
    override fun login(email: String, password: String): AuthorizationService.Tokens {
        val user = repository.findByIdOrNull(email)
        if (user == null || user.password != password) { // что-то пошло не так
            throw Exception("login error")
        }
        val tokens = generateTokens(email)
        user.accessToken = tokens.accessToken
        user.accessTokenElapsedAt = Instant.now().plus(Duration.ofMinutes(10)) // живет 10 минут
        user.refreshToken = tokens.refreshToken
        user.refreshTokenElapsedAt = Instant.now().plus(Duration.ofHours(1)) // пусть живет час
        repository.save(user)
        return tokens
    }

    override fun logout(email: String, token: String) {
        val user = repository.findByIdOrNull(email)
        if (user == null || user.refreshToken != token) {
            throw Exception("logout error")
        }
        if (user.refreshTokenElapsedAt!!.isBefore(Instant.now())) {
            throw Exception("refresh token elapsed")
        }
        user.accessToken = null;
        user.refreshToken = null;
        repository.save(user)
    }

    @Transactional
    @Throws(Exception::class)
    override fun register(email: String, password: String) {
        if (repository.findByIdOrNull(email) != null) { // пользователь уже есть
            throw Exception("this email is already used")
        }
        val newUser = UserEntity(email, password)
        repository.save(newUser)
    }

    @Transactional
    @Throws(Exception::class)
    override fun refresh(email: String, token: String): AuthorizationService.Tokens {
        val user = repository.findByIdOrNull(email)
        if (user == null || user.refreshToken != token) { // нет пользователя
            throw Exception("refresh token error")
        }
        if (user.refreshTokenElapsedAt!!.isBefore(Instant.now())) {
            throw Exception("refresh token elapsed")
        }
        val newTokens = generateTokens(email)
        user.refreshToken = newTokens.refreshToken
        user.refreshTokenElapsedAt = Instant.now().plus(Duration.ofHours(1)) // пусть живет час
        user.accessToken = newTokens.accessToken
        user.accessTokenElapsedAt = Instant.now().plus(Duration.ofMinutes(10)) // живет 10 минут
        repository.save(user)
        return newTokens
    }

    @Transactional
    @Throws(Exception::class)
    override fun validate(email: String, token: String): Boolean { // а у какого, простите, юзера??
        val user = repository.findByIdOrNull(email)
        return !(user == null || user.accessToken != token || user.accessTokenElapsedAt!!.isBefore(Instant.now()))
    }

    companion object{
        const val tokenLength = 32
    }
}
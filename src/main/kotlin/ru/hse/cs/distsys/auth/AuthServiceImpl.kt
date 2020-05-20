package ru.hse.cs.distsys.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder
import ru.hse.cs.distsys.AuthorizationError
import ru.hse.cs.distsys.email.SendEmailInterface
import java.lang.Exception
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
@Profile("auth")
class AuthServiceImpl(val repository: UserEntityRepository, val emailSender: SendEmailInterface) : AuthorizationService, AuthenticationService {
    private val secureRandom = SecureRandom()
    private val encoder = Base64.getUrlEncoder()
    @Value("\${kate.auth.baseurl}") lateinit var selfBaseUrl: String

    private fun generateTokens(email: String): AuthorizationService.Tokens {
        val bytes = ByteArray(tokenLength)
        secureRandom.nextBytes(bytes)
        val accessToken = encoder.encodeToString(bytes) + ";" + email
        secureRandom.nextBytes(bytes)
        val refreshToken = encoder.encodeToString(bytes)
        return AuthorizationService.Tokens(accessToken, refreshToken)
    }

    private fun generateOneToken(): String {
        val bytes = ByteArray(tokenLength)
        secureRandom.nextBytes(bytes)
        return encoder.encodeToString(bytes)
    }

    @Transactional
    @Throws(Exception::class)
    override fun login(email: String, password: String): AuthorizationService.Tokens {
        val user = repository.findByIdOrNull(email)
        if (user == null || user.password != password || !user.emailConfirmed) { // что-то пошло не так
            throw AuthorizationError("login error")
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
        if (user == null || user.refreshToken != token || !user.emailConfirmed) {
            throw AuthorizationError("logout error")
        }
        if (user.refreshTokenElapsedAt!!.isBefore(Instant.now())) {
            throw AuthorizationError("refresh token elapsed")
        }
        user.accessToken = null;
        user.refreshToken = null;
        repository.save(user)
    }

    @Transactional
    @Throws(Exception::class)
    override fun register(email: String, password: String) {
        if (repository.findByIdOrNull(email) != null) { // пользователь уже есть
            throw AuthorizationError("this email is already used")
        }
        val emailToken = generateOneToken()
        val confirmUrl = UriComponentsBuilder.fromHttpUrl(selfBaseUrl).path("/api/auth/confirm")
                .queryParam("email", email).queryParam("accessToken", emailToken).toUriString()
        emailSender.sendMail(email, "email confirm", confirmUrl)

        val newUser = UserEntity(email, password)
        newUser.emailConfirmToken = emailToken
        repository.save(newUser)
    }

    @Transactional
    @Throws(Exception::class)
    override fun refresh(email: String, token: String): AuthorizationService.Tokens {
        val user = repository.findByIdOrNull(email)
        if (user == null || !user.emailConfirmed || user.refreshToken != token) { // нет пользователя
            throw AuthorizationError("refresh token error")
        }
        if (user.refreshTokenElapsedAt!!.isBefore(Instant.now())) {
            throw AuthorizationError("refresh token elapsed")
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
    override fun validate(email: String, token: String): Boolean {
        val user = repository.findByIdOrNull(email) /*?: return false
        //println("correct token: " + user.accessToken + ", my token: " + token)
        //println("email confirmed: " + user.emailConfirmed)
        //println("tokens equal: " + user.accessToken == token)
        //println("validate ans: " + !(!user.emailConfirmed || user.accessToken != token || user.accessTokenElapsedAt!!.isBefore(Instant.now())))
        */
        return !(user == null || !user.emailConfirmed || user.accessToken != token || user.accessTokenElapsedAt!!.isBefore(Instant.now()))
    }

    @Transactional
    @Throws(Exception::class)
    override fun confirmEmail(email: String, token: String) {
        val user = repository.findByIdOrNull(email)
        if (user == null || user.emailConfirmToken != token) {
            throw AuthorizationError("confirm email error")
        }
        user.emailConfirmed = true
        user.emailConfirmToken = null
        repository.save(user)
    }

    companion object{
        const val tokenLength = 32
    }
}
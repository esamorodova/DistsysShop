package ru.hse.cs.distsys.auth

interface AuthorizationService {
    data class Tokens(val accessToken: String, val refreshToken: String)

    fun login(email: String, password: String): Tokens
    fun logout(email: String, token: String)
    fun register(email: String, password: String)
    fun refresh(email: String, token: String): Tokens
    fun confirmEmail(email: String, token: String)
}
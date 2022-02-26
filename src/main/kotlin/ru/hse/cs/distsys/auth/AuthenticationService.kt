package ru.hse.cs.distsys.auth

interface AuthenticationService {
    fun validate(email: String, token: String): Boolean
}
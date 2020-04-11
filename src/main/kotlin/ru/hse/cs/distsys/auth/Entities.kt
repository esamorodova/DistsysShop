package ru.hse.cs.distsys.auth

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
        @Id var email: String,
        var password: String, // АККУРАТНО?!?!
        var refreshToken: String? = null,
        var refreshTokenElapsedAt: Instant? = null,
        var accessToken: String? = null,
        var accessTokenElapsedAt: Instant? = null
)

package ru.hse.cs.distsys.auth

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
        @Id var email: String,
        var password: String, // АККУРАТНО?!?!
        @Column(name="refreshtoken") var refreshToken: String? = null,
        @Column(name="refreshtokenelapsedat") var refreshTokenElapsedAt: Instant? = null,
        @Column(name="accesstoken") var accessToken: String? = null,
        @Column(name="accesstokenelapsedat") var accessTokenElapsedAt: Instant? = null
)

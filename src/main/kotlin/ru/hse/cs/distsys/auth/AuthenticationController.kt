package ru.hse.cs.distsys.auth

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Profile("auth")
class AuthenticationController(val authenticationService: AuthenticationService, val authorizationService: AuthorizationService) {
    data class PasswordRequest(val email: String, val password: String)
    data class RefreshTokenRequest(val email: String, val refreshToken: String);
    data class AccessTokenRequest(val email: String, val accessToken: String);

    @PostMapping("/register")
    fun register(@RequestBody request: PasswordRequest) {
        authorizationService.register(request.email, request.password)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: PasswordRequest) : AuthorizationService.Tokens {
        return authorizationService.login(request.email, request.password)
    }

    @PostMapping("/logout")
    fun logout(@RequestBody request: RefreshTokenRequest) {
        authorizationService.logout(request.email, request.refreshToken)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshTokenRequest) : AuthorizationService.Tokens {
        return authorizationService.refresh(request.email, request.refreshToken)
    }

    @PostMapping("/validate")
    fun validate(@RequestBody request: AccessTokenRequest): Boolean {
        return authenticationService.validate(request.email, request.accessToken)
    }

    @GetMapping("/confirm")
    fun confirmEmail(@RequestParam email: String, @RequestParam accessToken: String) {
        authorizationService.confirmEmail(email, accessToken)
    }
}
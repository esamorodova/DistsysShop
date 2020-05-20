package ru.hse.cs.distsys.auth

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Profile("auth")
class AuthenticationController(val authenticationService: AuthenticationService, val authorizationService: AuthorizationService) {

    @PostMapping("/register")
    fun register(@RequestParam email: String, @RequestBody password: String) {
        authorizationService.register(email, password)
    }

    @PostMapping("/login")
    fun login(@RequestParam email: String, @RequestBody password: String) : AuthorizationService.Tokens {
        return authorizationService.login(email, password)
    }

    @PostMapping("/logout")
    fun logout(@RequestParam email: String, @RequestParam refreshToken: String) {
        authorizationService.logout(email, refreshToken)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestParam email: String, @RequestParam refreshToken: String) : AuthorizationService.Tokens {
        return authorizationService.refresh(email, refreshToken)
    }

    @PostMapping("/validate")
    fun validate(@RequestParam email: String, @RequestParam accessToken: String): Boolean {
        return authenticationService.validate(email, accessToken)
    }

    @GetMapping("/confirm")
    fun confirmEmail(@RequestParam email: String, @RequestParam accessToken: String) {
        authorizationService.confirmEmail(email, accessToken)
    }
}
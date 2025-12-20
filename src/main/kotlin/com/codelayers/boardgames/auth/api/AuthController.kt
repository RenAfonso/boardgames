package com.codelayers.boardgames.auth.api

import com.codelayers.boardgames.auth.security.JwtService
import com.codelayers.boardgames.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<TokenResponse> =
        ResponseEntity.ok(authService.login(request))

    @GetMapping("/validate")
    fun validate(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<ValidateResponse> {

        val token = authorization.removePrefix("Bearer ").trim()
        val claims = jwtService.extractClaims(token)

        return ResponseEntity.ok(authService.validate(claims))
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Void> =
        ResponseEntity.noContent().build()

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<RegisterResponse> {
        val response = authService.register(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/change-password")
    fun changePassword(
        @RequestBody request: ChangePasswordRequest,
        authentication: Authentication
    ): ResponseEntity<ChangePasswordResponse> {
        val username = authentication.name
        val response = authService.changePassword(username, request)
        return ResponseEntity.ok(response)
    }
}

package com.codelayers.boardgames.auth.service

import com.codelayers.boardgames.auth.api.ChangePasswordRequest
import com.codelayers.boardgames.auth.api.ChangePasswordResponse
import com.codelayers.boardgames.auth.api.LoginRequest
import com.codelayers.boardgames.auth.api.RegisterRequest
import com.codelayers.boardgames.auth.api.RegisterResponse
import com.codelayers.boardgames.auth.api.TokenResponse
import com.codelayers.boardgames.auth.api.ValidateResponse
import com.codelayers.boardgames.auth.entity.UserEntity
import com.codelayers.boardgames.auth.repository.RoleRepository
import com.codelayers.boardgames.auth.repository.UserRepository
import com.codelayers.boardgames.auth.security.DbUserDetails
import com.codelayers.boardgames.auth.security.JwtService
import io.jsonwebtoken.Claims
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    @Value("\${security.jwt.expiration-minutes}")
    private val expirationMinutes: Long,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(request: LoginRequest): TokenResponse {
        val authentication: Authentication =
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.username,
                    request.password
                )
            )

        val dbUser = authentication.principal as DbUserDetails

        val token = jwtService.generateToken(
            userId = dbUser.user.id,
            username = dbUser.username,
            roles = dbUser.authorities.map { it.authority } as Collection<String>
        )

        return TokenResponse(
            accessToken = token,
            expiresInMinutes = expirationMinutes
        )
    }

    fun validate(claims: Claims): ValidateResponse =
        ValidateResponse(
            userId = claims.subject,
            username = claims["username"].toString(),
            roles = claims["roles"] as List<String>
        )

    fun register(request: RegisterRequest): RegisterResponse {
        if (userRepository.existsByUsername(request.username) || userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Username or email already exists")
        }

        val defaultRole = roleRepository.findByName("ROLE_USER")
            ?: throw IllegalStateException("ROLE_USER not found")

        val user = UserEntity(
            id = UUID.randomUUID(),
            username = request.username,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password)!!,
            enabled = true,
            roles = setOf(defaultRole)
        )

        userRepository.save(user)

        return RegisterResponse(message = "User registered successfully")
    }

    fun changePassword(username: String, request: ChangePasswordRequest): ChangePasswordResponse {
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("User not found")

        if (!passwordEncoder.matches(request.oldPassword, user.passwordHash)) {
            throw IllegalArgumentException("Old password is incorrect")
        }

        user.passwordHash = passwordEncoder.encode(request.newPassword)!!
        userRepository.save(user)

        return ChangePasswordResponse(message = "Password changed successfully")
    }
}

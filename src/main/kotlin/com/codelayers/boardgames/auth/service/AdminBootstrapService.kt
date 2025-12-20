package com.codelayers.boardgames.auth.service

import com.codelayers.boardgames.auth.entity.UserEntity
import com.codelayers.boardgames.auth.repository.RoleRepository
import com.codelayers.boardgames.auth.repository.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AdminBootstrapService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostConstruct
    fun createAdminIfMissing() {
        val adminEmail = System.getenv("ADMIN_EMAIL").takeIf { !it.isNullOrBlank() } ?: return
        val adminUsername = System.getenv("ADMIN_USERNAME").takeIf { !it.isNullOrBlank() } ?: adminEmail
        val adminPassword = System.getenv("ADMIN_PASSWORD").takeIf { !it.isNullOrBlank() } ?: return

        if (userRepository.existsByEmail(adminEmail)) return

        val adminRole = roleRepository.findByName("ROLE_ADMIN")
            ?: throw IllegalStateException("ROLE_ADMIN not found")

        val adminUser = UserEntity(
            id = UUID.randomUUID(),
            username = adminUsername,
            email = adminEmail,
            passwordHash = passwordEncoder.encode(adminPassword)!!,
            enabled = true,
            roles = setOf(adminRole)
        )

        userRepository.save(adminUser)
    }
}
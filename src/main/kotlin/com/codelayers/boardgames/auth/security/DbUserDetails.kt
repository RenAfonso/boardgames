package com.codelayers.boardgames.auth.security

import com.codelayers.boardgames.auth.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DbUserDetails(
    val user: UserEntity
) : UserDetails {

    override fun getUsername(): String = user.username

    override fun getPassword(): String = user.passwordHash

    override fun getAuthorities(): Collection<GrantedAuthority> =
        user.roles.map { SimpleGrantedAuthority(it.name) }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = user.enabled

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = user.enabled
}
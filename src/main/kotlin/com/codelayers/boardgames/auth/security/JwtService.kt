package com.codelayers.boardgames.auth.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.Instant
import java.util.Date
import java.util.UUID

@Service
class JwtService(

    @Value("\${security.jwt.secret}")
    secret: String,

    @Value("\${security.jwt.expiration-minutes}")
    private val expirationMinutes: Long
) {

    private val key: Key =
        Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun generateToken(
        userId: UUID,
        username: String,
        roles: Collection<String>
    ): String {

        val now = Instant.now()
        val expiration = now.plusSeconds(expirationMinutes * 60)

        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("username", username)
            .claim("roles", roles)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiration))
            .signWith(key)
            .compact()
    }

    fun extractClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}

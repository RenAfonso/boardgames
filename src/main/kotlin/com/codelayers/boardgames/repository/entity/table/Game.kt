package com.codelayers.boardgames.repository.entity.table

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "games")
data class Game(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val code: String,

    @Column(nullable = false)
    val name: String,

    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL], orphanRemoval = true)
    val variants: List<GameVariant> = emptyList(),

    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL], orphanRemoval = true)
    val matches: List<Match> = emptyList()
)

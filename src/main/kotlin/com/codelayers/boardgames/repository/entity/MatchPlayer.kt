package com.codelayers.boardgames.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID

@Entity
@Table(name = "match_players", uniqueConstraints = [UniqueConstraint(columnNames = ["match_id", "user_id"])])
data class MatchPlayer(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: Match,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val points: Int,

    @Column(name = "starting_position", nullable = false)
    val startingPosition: Int,

    @Column(nullable = false)
    val won: Boolean,

    @OneToMany(mappedBy = "matchPlayer", cascade = [CascadeType.ALL], orphanRemoval = true)
    val dunePlayers: List<DuneMatchPlayer> = emptyList()
)

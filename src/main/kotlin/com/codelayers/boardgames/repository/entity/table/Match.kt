package com.codelayers.boardgames.repository.entity.table

import com.codelayers.boardgames.repository.entity.MatchTiebreaker
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "matches")
data class Match(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val game: Game,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    val variant: GameVariant? = null,

    @Column(name = "created_by", nullable = false)
    val createdBy: UUID,

    @OneToMany(mappedBy = "match", cascade = [CascadeType.ALL], orphanRemoval = true)
    var players: List<MatchPlayer> = emptyList(),

    @Column(name = "played_at", nullable = false)
    val playedAt: Instant,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tiebreaker: MatchTiebreaker = MatchTiebreaker.NONE
)

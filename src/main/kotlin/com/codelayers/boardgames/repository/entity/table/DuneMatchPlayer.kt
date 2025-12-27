package com.codelayers.boardgames.repository.entity.table

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "dune_match_players")
data class DuneMatchPlayer(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_player_id", nullable = false)
    val matchPlayer: MatchPlayer,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    val leader: DuneLeader,

    @Column(name = "team_side", nullable = false)
    val teamSide: String,  // 'Fremen' or 'Emperor'

    @Column(name = "spice_collected", nullable = false)
    val spiceCollected: Int = 0
)

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
@Table(
    name = "dune_leaders",
    uniqueConstraints = [UniqueConstraint(columnNames = ["variant_id", "name"])]
)
data class DuneLeader(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    val variant: GameVariant,

    @Column(nullable = false)
    val name: String,

    @OneToMany(mappedBy = "leader", cascade = [CascadeType.ALL], orphanRemoval = true)
    val matchPlayers: List<DuneMatchPlayer> = emptyList()
)

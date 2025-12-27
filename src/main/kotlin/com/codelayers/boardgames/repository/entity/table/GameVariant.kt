package com.codelayers.boardgames.repository.entity.table

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
@Table(name = "game_variants", uniqueConstraints = [UniqueConstraint(columnNames = ["game_id", "code"])])
data class GameVariant(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val game: Game,

    @Column(nullable = false)
    val code: String,

    @Column(nullable = false)
    val name: String,

    @OneToMany(mappedBy = "variant", cascade = [CascadeType.ALL], orphanRemoval = true)
    val duneLeaders: List<DuneLeader> = emptyList()
)

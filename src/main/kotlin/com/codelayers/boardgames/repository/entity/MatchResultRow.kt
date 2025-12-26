package com.codelayers.boardgames.repository.entity

import java.time.Instant

interface MatchResultRow {
    val won: Boolean
    val playedAt: Instant
}
package com.codelayers.boardgames.repository

import com.codelayers.boardgames.repository.entity.table.Match
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MatchRepository : JpaRepository<Match, UUID>

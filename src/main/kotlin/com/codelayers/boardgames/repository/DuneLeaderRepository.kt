package com.codelayers.boardgames.repository

import com.codelayers.boardgames.repository.entity.table.DuneLeader
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DuneLeaderRepository : JpaRepository<DuneLeader, UUID> {

    @Query("""
        SELECT dl
        FROM DuneLeader dl
        WHERE dl.variant.id = :variantId
          AND dl.name = :name
    """)
    fun findByVariantAndName(
        @Param("variantId") variantId: UUID,
        @Param("name") name: String
    ): DuneLeader?
}

package com.codelayers.boardgames.repository

import com.codelayers.boardgames.repository.entity.DuneMatchPlayerResult
import com.codelayers.boardgames.repository.entity.table.Game
import com.codelayers.boardgames.repository.entity.GameRankingRow
import com.codelayers.boardgames.repository.entity.LastMatchResultRow
import com.codelayers.boardgames.repository.entity.MatchPlayerResult
import com.codelayers.boardgames.repository.entity.table.MatchPlayer
import com.codelayers.boardgames.repository.entity.MatchResultRow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RankingRepository : JpaRepository<MatchPlayer, UUID> {

    @Query(
        """
    SELECT
        u.id AS userId,
        u.username AS username,

        COUNT(mp.id) AS gamesPlayed,
        SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END) AS wins,
        MAX(m.playedAt) AS lastPlayedAt

    FROM MatchPlayer mp
    JOIN mp.match m
    JOIN UserEntity u ON u.id = mp.userId

    WHERE m.game.code = :gameCode
      AND (:variantIds IS NULL OR m.variant.id IN :variantIds)

    GROUP BY u.id, u.username
    """
    )
    fun findOverallRanking(
        @Param("gameCode") gameCode: String,
        @Param("variantIds") variantIds: List<UUID>?
    ): List<GameRankingRow>

    @Query(
        """
    SELECT 
        u.id AS userId,
        u.username AS username,
        mp.points AS points,
        mp.won AS won,
        dmp.teamSide AS teamSide,
        dmp.matchPlayer.id AS matchPlayerId,
        m.playedAt AS playedAt,
        dl.name AS leaderName
    FROM DuneMatchPlayer dmp
    JOIN dmp.matchPlayer mp
    JOIN mp.match m
    JOIN UserEntity u ON u.id = mp.userId
    JOIN dmp.leader dl
    WHERE m.game.code = :gameCode
      AND u.id = :userId
      AND (:variantIds IS NULL OR m.variant.id IN :variantIds)
    ORDER BY m.playedAt DESC
    """
    )
    fun findDuneMatchPlayerResultsForUser(
        @Param("userId") userId: UUID,
        @Param("gameCode") gameCode: String,
        @Param("variantIds") variantIds: List<UUID>?
    ): List<DuneMatchPlayerResult>

    @Query(
        """
    SELECT
        u.id AS userId,
        u.username AS username,
        mp.points AS points,
        mp.won AS won,
        mp.id AS matchPlayerId,
        m.playedAt AS playedAt
    FROM MatchPlayer mp
    JOIN mp.match m
    JOIN UserEntity u ON u.id = mp.userId
    WHERE m.game.code = :gameCode
      AND u.id = :userId
      AND (:variantIds IS NULL OR m.variant.id IN :variantIds)
    ORDER BY m.playedAt DESC
    """
    )
    fun findMatchPlayerResultsForUser(
        @Param("userId") userId: UUID,
        @Param("gameCode") gameCode: String,
        @Param("variantIds") variantIds: List<UUID>?
    ): List<MatchPlayerResult>

    @Query(
        """
    SELECT
        u.id AS userId,
        u.username AS username,
        COUNT(mp.id) AS gamesPlayed,
        SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END) AS wins,
        MAX(m.playedAt) AS lastPlayedAt
    FROM MatchPlayer mp
    JOIN mp.match m
    JOIN UserEntity u ON u.id = mp.userId
    WHERE m.game.code = :gameCode
      AND u.id = :userId
      AND (:variantIds IS NULL OR m.variant.id IN :variantIds)
    GROUP BY u.id, u.username
    """
    )
    fun findOverallRankingForUser(
        @Param("userId") userId: UUID,
        @Param("gameCode") gameCode: String,
        @Param("variantIds") variantIds: List<UUID>?
    ): GameRankingRow?

    @Query(
        """
    SELECT mp.won AS won
    FROM MatchPlayer mp
    JOIN mp.match m
    WHERE mp.userId = :userId
      AND m.game.code = :gameCode
      AND (:variantIds IS NULL OR m.variant.id IN :variantIds)
    ORDER BY m.playedAt DESC
    """
    )
    fun findLastMatchResult(
        @Param("userId") userId: UUID,
        @Param("gameCode") gameCode: String,
        @Param("variantIds") variantIds: List<UUID>?
    ): List<LastMatchResultRow>

    @Query(
        """
    SELECT 
        mp.won AS won,
        m.playedAt AS playedAt
    FROM MatchPlayer mp
    JOIN mp.match m
    WHERE mp.userId = :userId
      AND m.game.code = :gameCode
      AND (:variantIds IS NULL OR m.variant.id IN :variantIds)
    ORDER BY m.playedAt DESC
    """
    )
    fun findMatchResultsForUser(
        @Param("userId") userId: UUID,
        @Param("gameCode") gameCode: String,
        @Param("variantIds") variantIds: List<UUID>?
    ): List<MatchResultRow>

    @Query(
        """
    SELECT gv.id
    FROM GameVariant gv
    WHERE gv.game.code = :gameCode
      AND gv.code IN :codes
    """
    )
    fun findIdsByGameAndCodes(
        @Param("gameCode") gameCode: String,
        @Param("codes") codes: Collection<String>
    ): List<UUID>

    @Query("""
    SELECT g, gv
    FROM Game g
    LEFT JOIN FETCH g.variants gv
""")
    fun findAllGamesWithVariants(): List<Game>
}

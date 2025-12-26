package com.codelayers.boardgames.service

import com.codelayers.boardgames.model.MatchResult
import com.codelayers.boardgames.model.UserRankingStats
import com.codelayers.boardgames.model.toUserRankingStats
import com.codelayers.boardgames.repository.RankingRepository
import com.codelayers.boardgames.repository.entity.MatchResultRow
import org.springframework.stereotype.Component

@Component
class RankingService(
    private val rankingRepository: RankingRepository
) {

    fun getOverallRanking(
        gameCode: String,
        variantCodes: List<String>
    ): List<UserRankingStats> {
        val variantIds = rankingRepository.findIdsByGameAndCodes(
            gameCode = gameCode,
            codes = variantCodes
        )

        val rankingRows = rankingRepository.findOverallRanking(
            gameCode = gameCode,
            variantIds = variantIds
        )

        return rankingRows.map { ranking ->

            val matchResults = rankingRepository.findMatchResultsForUser(
                userId = ranking.userId,
                gameCode = gameCode,
                variantIds = variantIds
            )

            val currentStreak = calculateCurrentStreak(matchResults)
            val lastMatch = lastMatchFrom(currentStreak)

            ranking.toUserRankingStats(
                lastMatch = lastMatch,
                currentStreak = currentStreak
            )
        }
    }

    private fun calculateCurrentStreak(results: List<MatchResultRow>): Int {
        if (results.isEmpty()) return 0

        val first = results.first().won
        var streak = 0

        for (result in results) {
            if (result.won == first) {
                streak++
            } else {
                break
            }
        }

        return if (first) streak else -streak
    }

    private fun lastMatchFrom(currentStreak: Int): MatchResult =
        when {
            currentStreak == 0 -> MatchResult.NONE
            currentStreak > 0 -> MatchResult.WIN
            else -> MatchResult.LOSS
        }
}

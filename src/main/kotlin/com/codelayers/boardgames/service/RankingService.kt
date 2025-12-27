package com.codelayers.boardgames.service

import com.codelayers.boardgames.auth.repository.UserRepository
import com.codelayers.boardgames.exception.UserNotFoundException
import com.codelayers.boardgames.model.DuneTeamUserRank
import com.codelayers.boardgames.model.GameUserRank
import com.codelayers.boardgames.model.MatchResult
import com.codelayers.boardgames.model.UserRankingStats
import com.codelayers.boardgames.model.UserStats
import com.codelayers.boardgames.model.toUserRankingStats
import com.codelayers.boardgames.repository.RankingRepository
import com.codelayers.boardgames.repository.entity.DuneMatchPlayerResult
import com.codelayers.boardgames.repository.entity.HasWinResult
import com.codelayers.boardgames.repository.entity.MatchPlayerResult
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

@Component
class RankingService(
    private val rankingRepository: RankingRepository,
    private val userRepository: UserRepository
) {

    companion object {
        private const val PAUL = "Muad'Dib"
        private const val SHADDAM = "Emperor Shaddam Corrino IV"
    }

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
                currentStreak = abs(currentStreak)
            )
        }
    }

    fun getGameRankingStats(
        gameCode: String,
        variantCodes: List<String>,
        userName: String,
        isDuneMatch: Boolean
    ): UserStats {
        val user = userRepository.findByUsername(userName) ?: throw UserNotFoundException("User $userName not found")
        val variantIds = rankingRepository.findIdsByGameAndCodes(
            gameCode = gameCode,
            codes = variantCodes
        )

        if (isDuneMatch) {
            val duneStatsList = rankingRepository.findDuneMatchPlayerResultsForUser(
                userId = user.id,
                gameCode = gameCode,
                variantIds = variantIds
            )

            return aggregateDuneRanking(duneStatsList)
        } else {
            val gameStatsList = rankingRepository.findMatchPlayerResultsForUser(
                userId = user.id,
                gameCode = gameCode,
                variantIds = variantIds
            )

            return aggregateGameRanking(gameStatsList)
        }
    }

    private fun aggregateGameRanking(matchResults: List<MatchPlayerResult>): GameUserRank {
        require(matchResults.isNotEmpty()) { "There must be at least one match result" }

        val userName = matchResults.first().username
        val gamesPlayed = matchResults.size
        val gamesWon = matchResults.count { it.won }
        val currentStreak = calculateCurrentStreak(matchResults)
        val lastMatch = lastMatchFrom(currentStreak)

        return GameUserRank(
            userName = userName,
            gamesPlayed = gamesPlayed,
            gamesWon = gamesWon,
            currentStreak = abs(currentStreak),
            lastMatch = lastMatch,
            winRate = winRate(gamesWon, gamesPlayed),
            averageVP = avg(matchResults.map { it.points })
        )
    }

    private fun aggregateDuneRanking(duneResults: List<DuneMatchPlayerResult>): DuneTeamUserRank {

        require(duneResults.isNotEmpty()) { "Cannot aggregate empty dune results" }

        val name = duneResults.first().username
        val gamesPlayed = duneResults.size
        val gamesWon = duneResults.count { it.won }

        val fremenGames = duneResults.filter { it.teamSide == "Fremen" }
        val emperorGames = duneResults.filter { it.teamSide == "Emperor" }

        val fremenWins = fremenGames.count { it.won }
        val emperorWins = emperorGames.count { it.won }

        val paulGames = duneResults.filter { it.leaderName == PAUL }
        val shaddamGames = duneResults.filter { it.leaderName == SHADDAM }

        val paulWins = paulGames.count { it.won }
        val shaddamWins = shaddamGames.count { it.won }

        val fremenPeasantGames = fremenGames.filter { it.leaderName != PAUL }
        val emperorPeasantGames = emperorGames.filter { it.leaderName != SHADDAM }

        val peasantGames = duneResults.filter {
            it.leaderName != PAUL && it.leaderName != SHADDAM
        }

        val leaderGames = duneResults.filter {
            it.leaderName == PAUL || it.leaderName == SHADDAM
        }

        val currentStreak = calculateCurrentStreak(duneResults)
        val lastMatch = lastMatchFrom(currentStreak)

        return DuneTeamUserRank(
            userName = name,

            gamesPlayed = gamesPlayed,
            gamesWon = gamesWon,
            winRate = winRate(gamesWon, gamesPlayed),

            winRateFremen = winRate(fremenWins, fremenGames.size),
            gamesPlayedFremenPeasant = fremenPeasantGames.size,

            winRateEmperor = winRate(emperorWins, emperorGames.size),
            gamesPlayedEmperorPeasant = emperorPeasantGames.size,

            winRatePaul = winRate(paulWins, paulGames.size),
            gamesPlayedPaul = paulGames.size,

            winRateShaddam = winRate(shaddamWins, shaddamGames.size),
            gamesPlayedShaddam = shaddamGames.size,

            averageVPPeasant = avg(peasantGames.map { it.points }),
            averageVPLeader = avg(leaderGames.map { it.points }),

            currentStreak = abs(currentStreak),
            lastMatch = lastMatch
        )
    }

    private fun <T : HasWinResult> calculateCurrentStreak(results: List<T>): Int {
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

    private fun winRate(wins: Int, games: Int): BigDecimal =
        if (games == 0) BigDecimal.ZERO
        else BigDecimal(wins).divide(BigDecimal(games), 4, RoundingMode.HALF_UP)

    private fun avg(points: List<Int>): BigDecimal =
        if (points.isEmpty()) BigDecimal.ZERO
        else BigDecimal(points.sum())
            .divide(BigDecimal(points.size), 4, RoundingMode.HALF_UP)
}

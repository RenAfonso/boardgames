package com.codelayers.boardgames.controller

import com.codelayers.boardgames.controller.dto.GameRankingResponse
import com.codelayers.boardgames.model.UserRank
import com.codelayers.boardgames.service.GameCatalogService
import com.codelayers.boardgames.service.RankingService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/rank")
class UserController(
    private val gameCatalogService: GameCatalogService,
    private val rankingService: RankingService
) {

    @GetMapping
    fun getUserRank(): ResponseEntity<UserRank> =
        ResponseEntity.ok().body(
            UserRank(
                name = "ren",
                winRate = BigDecimal("62.50"),
                gamesPlayed = 40,
                gamesWon = 25,
                winRateFremen = BigDecimal("70.00"),
                gamesPlayedFremenPeasant = 10,
                winRateEmperor = BigDecimal("55.56"),
                gamesPlayedEmperorPeasant = 9,
                winRatePaul = BigDecimal("60.00"),
                gamesPlayedPaul = 10,
                winRateShaddam = BigDecimal("50.00"),
                gamesPlayedShaddam = 11,
                averageVPPeasant = BigDecimal("8.25"),
                averageVPLeader = BigDecimal("10.75"),
                currentStreak = "W3"
            )
        )

    @GetMapping("/{game}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getFullGameRankings(
        @PathVariable("game") game: String,
        @RequestParam("variant", required = false) variants: List<String> = emptyList()
    ): ResponseEntity<GameRankingResponse>  {
        val gameRankingResponse = rankingService.getOverallRanking(
            gameCode = game,
            variantCodes = variants
        ).let { stats ->
            GameRankingResponse(
                game = mapGameName(game),
                variants = mapGameVariants(game, variants),
                stats = stats
            )
        }

        return ResponseEntity.ok().body(gameRankingResponse)
    }

    private fun mapGameName(gameCode: String): String =
        gameCatalogService.getGame(gameCode).gameName

    private fun mapGameVariants(gameCode: String, variantCodes: List<String>): List<String> =
        gameCatalogService.getVariantNames(gameCode, variantCodes)
}
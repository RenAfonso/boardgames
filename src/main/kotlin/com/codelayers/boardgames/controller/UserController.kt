package com.codelayers.boardgames.controller

import com.codelayers.boardgames.controller.model.UserRank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/rank")
class UserController {

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
}
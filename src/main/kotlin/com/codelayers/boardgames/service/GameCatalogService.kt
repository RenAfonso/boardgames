package com.codelayers.boardgames.service

import com.codelayers.boardgames.auth.repository.UserRepository
import com.codelayers.boardgames.controller.dto.GameType
import com.codelayers.boardgames.controller.dto.MatchRequest
import com.codelayers.boardgames.model.GameMeta
import com.codelayers.boardgames.model.VariantMeta
import com.codelayers.boardgames.repository.DuneLeaderRepository
import com.codelayers.boardgames.repository.MatchRepository
import com.codelayers.boardgames.repository.RankingRepository
import com.codelayers.boardgames.repository.entity.table.DuneMatchPlayer
import com.codelayers.boardgames.repository.entity.table.Game
import com.codelayers.boardgames.repository.entity.table.GameVariant
import com.codelayers.boardgames.repository.entity.table.Match
import com.codelayers.boardgames.repository.entity.table.MatchPlayer
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameCatalogService(
    private val duneLeaderRepository: DuneLeaderRepository,
    private val matchRepository: MatchRepository,
    private val rankingRepository: RankingRepository,
    private val userRepository: UserRepository
) {

    @Volatile
    private var gameCache: Map<String, GameMeta> = emptyMap()

    @PostConstruct
    fun loadCache() {
        refresh()
    }

    @Synchronized
    fun refresh() {
        gameCache = rankingRepository.findAllGamesWithVariants()
            .associate { game ->
                game.code to GameMeta(
                    gameId = game.id!!,
                    gameCode = game.code,
                    gameName = game.name,
                    variantsByCode = game.variants.associate { variant ->
                        variant.code to VariantMeta(
                            variantId = variant.id!!,
                            code = variant.code,
                            name = variant.name
                        )
                    }
                )
            }
    }

    fun getGame(gameCode: String): GameMeta =
        gameCache[gameCode]
            ?: throw IllegalArgumentException("Unknown game: $gameCode")

    fun getVariantIds(gameCode: String, variantCodes: List<String>): List<UUID> {
        val game = getGame(gameCode)
        return variantCodes.map { code ->
            game.variantsByCode[code]?.variantId
                ?: throw IllegalArgumentException("Unknown variant $code for game $gameCode")
        }
    }

    fun getVariantNames(gameCode: String, variantCodes: List<String>): List<String> {
        val game = getGame(gameCode)
        return variantCodes.map { code ->
            game.variantsByCode[code]?.name
                ?: throw IllegalArgumentException("Unknown variant $code for game $gameCode")
        }
    }

    fun insertGame(matchRequest: MatchRequest, creator: String, gameCode: String, variantCodes: List<String>) {
        val userId = UUID.fromString(creator)
        val game = rankingRepository.findGameByCode(gameCode) ?: throw IllegalArgumentException("Unknown game: $gameCode")
        val gameVariant = rankingRepository.findVariantByGameCodeAndVariantCode(gameCode, variantCodes.first()) ?: throw IllegalArgumentException("Unknown variant: $variantCodes")

        when (matchRequest.gameType) {

            GameType.GENERAL -> processGeneral(
                request = matchRequest,
                creator = userId,
                game = game,
                gameVariant = gameVariant
            )

            GameType.DUNE -> processDune(
                request = matchRequest,
                creator = userId,
                game = game,
                gameVariant = gameVariant
            )

            GameType.DUNE_TEAM -> processDuneTeam(
                request = matchRequest,
                creator = userId,
                game = game,
                gameVariant = gameVariant
            )

            else -> throw IllegalArgumentException("Unknown game: $matchRequest")
        }
    }

    private fun processGeneral(request: MatchRequest, creator: UUID, game: Game, gameVariant: GameVariant) {
        val match = Match(
            game = game,
            variant = gameVariant,
            createdBy = creator,
            playedAt = request.playedAt,
            tiebreaker = request.tieBreak,
            players = emptyList()
        )

        val players = request.players.map { player ->
            MatchPlayer(
                match = match,
                userId = userRepository.findByUsername(player.username)?.id
                    ?: throw IllegalArgumentException("Unknown user: ${player.username}"),
                points = player.vp,
                startingPosition = player.startingPosition,
                won = player.username == request.winner
            )
        }

        match.players = players
        matchRepository.save(match)
    }

    private fun processDune(
        request: MatchRequest,
        creator: UUID,
        game: Game,
        gameVariant: GameVariant
    ) {
        require(request.players.size == 4 || request.players.size == 3) {
            "DUNE requires 3 or 4 players"
        }

        require(request.players.all { it.team == null }) {
            "DUNE (non-team) must not define playerTeam"
        }

        val match = Match(
            game = game,
            variant = gameVariant,
            createdBy = creator,
            playedAt = request.playedAt,
            tiebreaker = request.tieBreak,
            players = emptyList()
        )

        val matchPlayers = request.players.map { player ->

            val userId = userRepository.findByUsername(player.username)?.id
                ?: throw IllegalArgumentException("Unknown user: ${player.username}")

            val leaderName = player.leader
                ?: throw IllegalArgumentException("DUNE player must have a leader")

            val leader = duneLeaderRepository
                .findByVariantAndName(gameVariant.id!!, leaderName)
                ?: throw IllegalArgumentException("Unknown leader '$leaderName' for variant ${gameVariant.code}")

            val matchPlayer = MatchPlayer(
                match = match,
                userId = userId,
                points = player.vp,
                startingPosition = player.startingPosition,
                won = player.username == request.winner
            )

            val dunePlayer = DuneMatchPlayer(
                matchPlayer = matchPlayer,
                leader = leader,
                teamSide = "None"
            )

            matchPlayer.dunePlayers = listOf(dunePlayer)
            return@map matchPlayer
        }

        match.players = matchPlayers
        matchRepository.save(match)
    }

    private fun processDuneTeam(
        request: MatchRequest,
        creator: UUID,
        game: Game,
        gameVariant: GameVariant
    ) {
        require(request.players.size == 6) {
            "DUNE requires 6 players when played in teams"
        }

        require(request.players.all { it.team == "Fremen" || it.team == "Emperor" }) {
            "DUNE_TEAM must define team"
        }

        val match = Match(
            game = game,
            variant = gameVariant,
            createdBy = creator,
            playedAt = request.playedAt,
            tiebreaker = request.tieBreak,
            players = emptyList()
        )

        val matchPlayers = request.players.map { player ->

            val userId = userRepository.findByUsername(player.username)?.id
                ?: throw IllegalArgumentException("Unknown user: ${player.username}")

            val leaderName = player.leader
                ?: throw IllegalArgumentException("DUNE player must have a leader")

            val leader = duneLeaderRepository
                .findByVariantAndName(gameVariant.id!!, leaderName)
                ?: throw IllegalArgumentException("Unknown leader '$leaderName' for variant ${gameVariant.code}")

            val matchPlayer = MatchPlayer(
                match = match,
                userId = userId,
                points = player.vp,
                startingPosition = player.startingPosition,
                won = player.team == request.winner
            )

            val dunePlayer = DuneMatchPlayer(
                matchPlayer = matchPlayer,
                leader = leader,
                teamSide = player.team!!
            )

            matchPlayer.dunePlayers = listOf(dunePlayer)
            return@map matchPlayer
        }

        match.players = matchPlayers
        matchRepository.save(match)
    }
}

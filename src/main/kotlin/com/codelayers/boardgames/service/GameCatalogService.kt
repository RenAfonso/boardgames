package com.codelayers.boardgames.service

import com.codelayers.boardgames.model.GameMeta
import com.codelayers.boardgames.model.VariantMeta
import com.codelayers.boardgames.repository.RankingRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameCatalogService(
    private val repository: RankingRepository
) {

    @Volatile
    private var gameCache: Map<String, GameMeta> = emptyMap()

    @PostConstruct
    fun loadCache() {
        refresh()
    }

    @Synchronized
    fun refresh() {
        gameCache = repository.findAllGamesWithVariants()
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
}

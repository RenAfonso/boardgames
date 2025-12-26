package com.codelayers.boardgames.model

import java.util.UUID

data class GameMeta(
    val gameId: UUID,
    val gameCode: String,
    val gameName: String,
    val variantsByCode: Map<String, VariantMeta>
)

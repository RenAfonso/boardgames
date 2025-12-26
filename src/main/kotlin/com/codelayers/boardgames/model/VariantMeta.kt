package com.codelayers.boardgames.model

import java.util.UUID

data class VariantMeta(
    val variantId: UUID,
    val code: String,
    val name: String
)

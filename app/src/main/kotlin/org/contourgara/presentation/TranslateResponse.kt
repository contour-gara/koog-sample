package org.contourgara.presentation

import kotlinx.serialization.Serializable

@Serializable
data class TranslateResponse(
    private val translatedText: String,
)

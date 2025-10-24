package org.contourgara.presentation

import kotlinx.serialization.Serializable

@Serializable
data class TranslateRequest(
    private val text: String,
)

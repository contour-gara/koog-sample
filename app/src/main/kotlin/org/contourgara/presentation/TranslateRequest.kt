package org.contourgara.presentation

import kotlinx.serialization.Serializable

@Serializable
data class TranslateRequest(
    val text: String,
)

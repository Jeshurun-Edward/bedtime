package com.example.bedtime1.data

import kotlinx.serialization.Serializable

@Serializable
data class PromptResponse (
    val title : String,
    val story : String,
    val moral : String,
    val imagePrompt : String
)
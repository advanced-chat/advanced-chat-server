package org.advancedchat.application.ui.model

data class LoadingBarConfiguration(
    val barThickness: Int,
    val barColors: Map<String, String>,
    val shadowBlur: Double,
    val shadowColor: String,
)

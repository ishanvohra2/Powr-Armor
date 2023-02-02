package com.ishanvohra.powrarmor.models

/**
 * Response model for getArmor() API
 */
data class ArmorResponseItem(
    val assets: Assets?,
    val defense: Defense,
    val id: Int,
    val name: String,
    val slots: List<Slot>
)

data class Assets(
    val imageFemale: String,
    val imageMale: String
)

data class Defense(
    val base: Int
)

data class Slot(
    val rank: Int
)
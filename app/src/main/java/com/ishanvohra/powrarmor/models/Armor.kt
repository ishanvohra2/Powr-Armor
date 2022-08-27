package com.ishanvohra.powrarmor.models

class ArmorResponse : ArrayList<ArmorResponseItem>()

/**
 * Response model for getArmor() API
 */
data class ArmorResponseItem(
    val assets: Assets?,
    val defense: Defense,
    val id: Int,
    val name: String,
    val rank: String,
    val slots: List<Slot>,
    val type: String
)

data class Assets(
    val imageFemale: String,
    val imageMale: String
)

data class Defense(
    val augmented: Int,
    val base: Int,
    val max: Int
)

data class Slot(
    val rank: Int
)
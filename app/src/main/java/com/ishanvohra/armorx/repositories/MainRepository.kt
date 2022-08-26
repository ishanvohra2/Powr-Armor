package com.ishanvohra.armorx.repositories

import com.ishanvohra.armorx.networking.RetrofitClient

class MainRepository {

    suspend fun getArmorPieces() = RetrofitClient().instance.getArmorPieces()

}
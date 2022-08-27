package com.ishanvohra.powrarmor.repositories

import com.ishanvohra.powrarmor.networking.RetrofitClient

class MainRepository {

    suspend fun getArmorPieces() = RetrofitClient().instance.getArmorPieces()

}
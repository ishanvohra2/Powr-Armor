package com.ishanvohra.armorx.networking

import com.ishanvohra.armorx.models.ArmorResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET("armor")
    suspend fun getArmorPieces(): Response<ArmorResponse>

}
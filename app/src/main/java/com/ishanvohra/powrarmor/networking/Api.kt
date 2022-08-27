package com.ishanvohra.powrarmor.networking

import com.ishanvohra.powrarmor.models.ArmorResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET("armor")
    suspend fun getArmorPieces(): Response<ArmorResponse>

}
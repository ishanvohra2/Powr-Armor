package com.ishanvohra.armorx.networking

import com.ishanvohra.armorx.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    //creating retrofit instance
    val instance: Api by lazy {

        //defining http client properties
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(NetworkInterceptor())
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }

}
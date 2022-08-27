package com.ishanvohra.powrarmor.networking

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Custom interceptor class for the retrofit client
 */
class NetworkInterceptor: Interceptor {

    companion object{
        const val TAG = "NetworkInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        /**
         * Catch exceptions thrown while sending the request
         */
        return try {
            chain.proceed(builder.build())
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())

            Response
                .Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .message("")
                .code(499)
                .body("${e.message}".toResponseBody(null))
                .build()
        }
    }
}
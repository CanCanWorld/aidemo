package com.zrq.aidemo.common.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("chat/completions")
     fun getChat(
        @Body map: MutableMap<String, Any>
    ): Response<String>
}
package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class CotacaoResponse(val date: String, val rates: Map<String, Double>)

interface CotacaoApi {
    @GET("v1/latest")
    suspend fun cotacao(
        @Query("base") base: String = "USD",
        @Query("symbols") symbols: String = "BRL"
    ): CotacaoResponse
}

object CotacaoClient {
    val api: CotacaoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.frankfurter.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CotacaoApi::class.java)
    }
}

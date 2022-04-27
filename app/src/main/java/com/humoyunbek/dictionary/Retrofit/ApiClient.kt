package com.humoyunbek.dictionary.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
    fun getRetrofit(baseUrl:String):Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun apiService():ApiService{
        return getRetrofit(BASE_URL).create(ApiService::class.java)
    }
}
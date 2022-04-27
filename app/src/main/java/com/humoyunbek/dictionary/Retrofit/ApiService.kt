package com.humoyunbek.dictionary.Retrofit

import com.humoyunbek.dictionary.models.WordModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{word}")
    suspend fun getMyWord(@Path("word") word:String):List<WordModel>
}
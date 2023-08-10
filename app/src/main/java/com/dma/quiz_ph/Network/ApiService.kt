package com.dma.quiz_ph.Network

import com.dma.quiz_ph.Model.QnAResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("quiz.json")
    suspend fun getQnA():Response<QnAResponse>
}
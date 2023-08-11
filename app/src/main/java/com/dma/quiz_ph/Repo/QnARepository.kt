package com.dma.quiz_ph.Repo

import android.util.Log
import com.dma.quiz_ph.Model.QnAResponse
import com.dma.quiz_ph.Network.ApiService
import com.dma.quiz_ph.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class QnARepository @Inject constructor(private val apiService: ApiService)  {
    suspend fun getQnA() = flow {

        emit(DataStatus.loading())
        val result = apiService.getQnA()
        when (result.code()) {
            200 -> emit(DataStatus.success(result.body()))
            401 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }
        .catch {
            emit(DataStatus.error(it.message.toString()))
        }
        .flowOn(Dispatchers.IO)

}
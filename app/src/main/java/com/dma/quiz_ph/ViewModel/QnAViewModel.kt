package com.dma.quiz_ph.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dma.quiz_ph.Model.QnAResponse
import com.dma.quiz_ph.Repo.QnARepository
import com.dma.quiz_ph.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QnAViewModel  @Inject constructor(private val repository: QnARepository): ViewModel() {

    private val _qnaResponse = MutableLiveData<DataStatus<QnAResponse>>()
    val qnaResponse : LiveData<DataStatus<QnAResponse>>
    get() = _qnaResponse

    fun getQnA() = viewModelScope.launch {
        repository.getQnA().collect{
            _qnaResponse.value = it
        }
    }
}
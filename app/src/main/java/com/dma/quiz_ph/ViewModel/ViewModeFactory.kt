package com.dma.quiz_ph.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dma.quiz_ph.Repo.QnARepository
import javax.inject.Inject

class ViewModeFactory @Inject constructor(private val repository: QnARepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QnAViewModel(repository) as T
    }
}
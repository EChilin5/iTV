package com.example.itv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itv.repository.Repository


class SampleApiViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  ApiSampleViewModel(repository) as T
    }
}
package com.example.itv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itv.model.Post
import com.example.itv.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiSampleViewModel(private val repository: Repository): ViewModel() {
    val myResponse: MutableLiveData<Response<Post>> = MutableLiveData()

    fun getPost(){
       viewModelScope.launch{
            val response: Response<Post> = repository.getPost()
            myResponse.value = response
       }
    }
}
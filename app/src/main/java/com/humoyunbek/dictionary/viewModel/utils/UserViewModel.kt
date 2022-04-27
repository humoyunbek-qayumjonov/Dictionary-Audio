package com.humoyunbek.dictionary.viewModel.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humoyunbek.dictionary.Retrofit.ApiClient
import com.humoyunbek.dictionary.models.WordModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class UserViewModel:ViewModel() {
    private val liveData  = MutableLiveData<Resource<List<WordModel>>>()


    fun getWord(word:String):LiveData<Resource<List<WordModel>>>{
        val apiService = ApiClient.apiService()
        viewModelScope.launch {
            liveData.postValue(Resource.loading(null))
            try {
                coroutineScope {
                    val async = async {
                        apiService.getMyWord(word)
                    }
                    val await = async.await()

                    liveData.postValue(Resource.success(await))
                }
            }catch (e:Exception){
                liveData.postValue(Resource.error(e.message ?: "Error",null))
            }
        }
        return liveData
    }
}
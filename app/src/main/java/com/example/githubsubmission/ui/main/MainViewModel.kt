package com.example.githubsubmission.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsubmission.data.response.GitHubResponse
import com.example.githubsubmission.data.response.ItemsItem
import com.example.githubsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainViewModel:ViewModel() {

    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers : LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    var errorMessage = ""

    companion object {
        const val TAG = "MainViewModel"
    }

    init {
        findProfiles()
    }

    fun setName(newName: String) {
        _name.value = newName
        findProfiles()
    }

    private fun findProfiles(){
        _isError.value=false
        _isLoading.value = true
        val client = ApiConfig.getApiService().getProfiles(name.value ?: "a")
        client.enqueue(object : Callback, retrofit2.Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody!=null){
                        _listUsers.value = response.body()?.items
                    }else{
                        errorMessage = response.message()
                        _isError.value=true
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
                else {
                    errorMessage = response.message()
                    _isError.value=true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                errorMessage = t.message.toString()
                _isError.value=true
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        }
        )
    }
}
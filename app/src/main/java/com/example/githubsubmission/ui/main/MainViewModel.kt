package com.example.githubsubmission.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsubmission.data.response.GitHubResponse
import com.example.githubsubmission.data.response.ItemsItem
import com.example.githubsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    var errorMessage = "Please check your connectivity!"

    init {
        findProfiles()
    }

    fun setName(newName: String) {
        _name.value = newName
        findProfiles()
    }

    private fun findProfiles() {
        _isError.value = false
        _isLoading.value = true

        val nameValue = _name.value ?: "a" // Get the current value of name LiveData

        val client = ApiConfig.getApiService().getProfiles(nameValue)
        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUsers.value = responseBody.items
                    } else {
                        errorMessage = "Response body is null"
                        _isError.value = true
                    }
                } else {
                    errorMessage = "Unsuccessful response: ${response.code()}"
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                errorMessage = t.message ?: "Unknown error"
                _isError.value = true
                _isLoading.value = false
            }
        })
    }

    companion object {
        const val TAG = "MainViewModel"
    }

}
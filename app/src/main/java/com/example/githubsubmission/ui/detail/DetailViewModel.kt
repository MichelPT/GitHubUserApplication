package com.example.githubsubmission.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubsubmission.data.response.DetailUserResponse
import com.example.githubsubmission.data.response.ItemsItem
import com.example.githubsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class DetailViewModel(val username: String) : ViewModel() {
    private val _followCount = MutableLiveData<Pair<String, String>>()
    val followCount: LiveData<Pair<String, String>> = _followCount

    private val _followingUser = MutableLiveData<List<ItemsItem>>()
    val followingUser: LiveData<List<ItemsItem>> = _followingUser

    private val _followerUser = MutableLiveData<List<ItemsItem>>()
    val followerUser: LiveData<List<ItemsItem>> = _followerUser

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private val _realName = MutableLiveData<String>()
    val realName: LiveData<String> = _realName

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    var errorMessage = ""
    var detailedId : Int? = null

    init {
        findProfiles()
        findFollowing()
        findFollowers()
    }

    class DetailViewModelFactory(private val username: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(username) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun findProfiles() {
        _isError.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback, retrofit2.Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        detailedId = responseBody.id
                        val followingCount = responseBody.following
                        val followersCount = responseBody.followers
                        _followCount.value =
                            Pair(followingCount.toString(), followersCount.toString())
                        _imageUrl.value = responseBody.avatarUrl!!
                        _realName.value = responseBody.name ?: "Name"
                    } else {
                        errorMessage = response.message()
                        _isError.value = true
                        Log.e("DetailUserActivity", "onFailure: ${response.message()}")
                    }
                } else {
                    errorMessage = response.message()
                    _isError.value = true
                    Log.e("DetailUserActivity", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                errorMessage = t.message.toString()
                _isError.value = true
                _isLoading.value = false
                Log.e("DetailUserActivity", "onFailure: ${t.message}")
            }

        })
    }

    private fun findFollowing() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback, retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followingUser.value = responseBody!!
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("DetailUserActivity", "onFailure: ${t.message}")
            }

        })

    }

    private fun findFollowers() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback, retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followerUser.value = responseBody!!
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("DetailUserActivity", "onFailure: ${t.message}")
            }

        })

    }

}
package com.example.githubsubmission.data.retrofit

import com.example.githubsubmission.data.response.DetailUserResponse
import com.example.githubsubmission.data.response.GitHubResponse
import com.example.githubsubmission.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getProfiles(
        @Query("q") name: String
    ): Call<GitHubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username : String
    ) : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}
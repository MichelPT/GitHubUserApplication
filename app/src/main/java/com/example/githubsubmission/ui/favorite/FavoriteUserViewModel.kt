package com.example.githubsubmission.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubsubmission.database.FavoriteUser
import com.example.githubsubmission.repository.FavoriteRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {

    var mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun insert(favorite: FavoriteUser) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: FavoriteUser) {
        mFavoriteRepository.delete(favorite)
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteRepository.getAllFavorites()
    fun getFavoriteUser(username: String): LiveData<FavoriteUser> =
        mFavoriteRepository.getFavoriteUserByUsername(username)
}
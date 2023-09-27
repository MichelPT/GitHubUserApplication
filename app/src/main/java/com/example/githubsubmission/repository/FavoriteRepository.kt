package com.example.githubsubmission.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubsubmission.database.FavoriteDao
import com.example.githubsubmission.database.FavoriteRoomDatabase
import com.example.githubsubmission.database.FavoriteUser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }
    fun getAllFavorites(): LiveData<List<FavoriteUser>> = mFavoriteDao.getAllNotes()
    fun insert(favorite: FavoriteUser) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }
    fun delete(favorite: FavoriteUser) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }
    fun update(favorite: FavoriteUser) {
        executorService.execute { mFavoriteDao.update(favorite) }
    }
    fun getFavoriteUserByUsername(username:String):LiveData<FavoriteUser> = mFavoriteDao.getFavoriteUserByUsername(username)
}
package com.example.githubsubmission.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubsubmission.databinding.ActivityFavoriteUserBinding
import com.example.githubsubmission.helper.ViewModelFactory


class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var addFavoriteUserViewModel: FavoriteUserViewModel
    private var _favoriteUserBinding: ActivityFavoriteUserBinding? = null
    private val binding get() = _favoriteUserBinding
    private val favoriteViewModel by viewModels<FavoriteUserViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _favoriteUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        addFavoriteUserViewModel = obtainViewModel(this@FavoriteUserActivity)


    }

    override fun onDestroy() {
        super.onDestroy()
        _favoriteUserBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }
}
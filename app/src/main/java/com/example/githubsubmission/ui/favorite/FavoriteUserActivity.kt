package com.example.githubsubmission.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsubmission.data.response.ItemsItem
import com.example.githubsubmission.databinding.ActivityFavoriteUserBinding
import com.example.githubsubmission.helper.ViewModelFactory
import com.example.githubsubmission.ui.adapter.ProfileListAdapter
import com.example.githubsubmission.ui.detail.DetailUserActivity


class FavoriteUserActivity : AppCompatActivity() {
    private var _favoriteUserBinding: ActivityFavoriteUserBinding? = null
    private val binding get() = _favoriteUserBinding
    private val favoriteViewModel by viewModels<FavoriteUserViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var detailAdapter: ProfileListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Favorite Users"
        _favoriteUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        setProfilesList()
        showRecycleView()
    }

    private fun setProfilesList() {
        favoriteViewModel.getAllFavoriteUser().observe(this@FavoriteUserActivity) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(
                    login = it.username,
                    avatarUrl = it.avatarUrl!!,
                    id = it.id!!,
                    followersUrl = "",
                    followingUrl = ""
                )
                items.add(item)
            }
            detailAdapter.submitList(items)
        }
    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
        binding?.rvFavorites?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvFavorites?.addItemDecoration(itemDecoration)
        detailAdapter = ProfileListAdapter()
        binding?.rvFavorites?.adapter = detailAdapter
        detailAdapter.setOnItemClickCallback(object : ProfileListAdapter.OnItemClickCallBack {
            override fun onItemClicked(profile: ItemsItem) {
                val moveProfileDetail =
                    Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                moveProfileDetail.putExtra("username", profile.login)
                startActivity(moveProfileDetail)
            }

        }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _favoriteUserBinding = null
    }
}
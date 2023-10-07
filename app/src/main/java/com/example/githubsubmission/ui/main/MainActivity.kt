package com.example.githubsubmission.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsubmission.R
import com.example.githubsubmission.data.datastore.SettingPreferences
import com.example.githubsubmission.data.datastore.SwitchViewModelFactory
import com.example.githubsubmission.data.datastore.dataStore
import com.example.githubsubmission.data.response.ItemsItem
import com.example.githubsubmission.databinding.ActivityMainBinding
import com.example.githubsubmission.ui.adapter.ProfileListAdapter
import com.example.githubsubmission.ui.detail.DetailUserActivity
import com.example.githubsubmission.ui.favorite.FavoriteUserActivity
import com.example.githubsubmission.ui.modeSwitch.ModeSwitch
import com.example.githubsubmission.ui.modeSwitch.ModeSwitchViewModel

class  MainActivity : AppCompatActivity() {

    private var _mainActivityBinding: ActivityMainBinding? = null
    private val binding get() = _mainActivityBinding
    private lateinit var detailAdapter: ProfileListAdapter
    private lateinit var viewModel: MainViewModel
    private var searchedName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding?.searchView?.setupWithSearchBar(binding?.searchBar)
        binding?.searchView?.editText?.setOnEditorActionListener { _, _, _ ->
            binding?.searchBar?.text = binding?.searchView?.text
            binding?.searchView?.hide()
            searchedName = binding?.searchView?.text.toString()
            Log.d("MainActivity", "Searched Name: $searchedName")
            searchAndFetchData(searchedName)
            false
        }


        viewModel.isError.observe(this) { isError ->
            if (isError) {
                binding?.tvError?.text = getString(R.string.error, viewModel.errorMessage)
                binding?.imgError?.visibility = View.VISIBLE
                binding?.tvError?.visibility = View.VISIBLE
                binding?.rvProfiles?.visibility = View.INVISIBLE
            } else {
                binding?.imgError?.visibility = View.INVISIBLE
                binding?.tvError?.visibility = View.INVISIBLE
                viewModel.listUsers.observe(this) { consumerUsers ->
                    setProfilesList(consumerUsers)
                }
            }
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showRecycleView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_menu -> {
                val moveIntent = Intent(this, FavoriteUserActivity::class.java)
                startActivity(moveIntent)
            }

            R.id.switch_menu -> {
                val moveIntent = Intent(this, ModeSwitch::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setProfilesList(consumerProfiles: List<ItemsItem>) {
        detailAdapter.submitList(consumerProfiles)
    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding?.rvProfiles?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvProfiles?.addItemDecoration(itemDecoration)
        detailAdapter = ProfileListAdapter()
        binding?.rvProfiles?.adapter = detailAdapter
        detailAdapter.setOnItemClickCallback(object : ProfileListAdapter.OnItemClickCallBack {
            override fun onItemClicked(profile: ItemsItem) {
                val moveProfileDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                moveProfileDetail.putExtra("username", profile.login)
                startActivity(moveProfileDetail)
            }

        }
        )
    }

    private fun searchAndFetchData(searchedName: String) {
        viewModel.setName(searchedName)
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _mainActivityBinding = null
    }
}
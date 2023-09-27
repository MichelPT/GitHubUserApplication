package com.example.githubsubmission.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsubmission.R
import com.example.githubsubmission.data.response.ItemsItem
import com.example.githubsubmission.database.FavoriteRoomDatabase
import com.example.githubsubmission.databinding.ActivityMainBinding
import com.example.githubsubmission.ui.detail.DetailUserActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var detailAdapter: ProfileListAdapter
    private lateinit var viewModel: MainViewModel
    private var searchedName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.text = searchView.text
                searchView.hide()
                searchedName = searchView.text.toString()
                Log.d("MainActivity", "Searched Name: $searchedName")
                searchAndFetchData(searchedName)
                false
            }
        }

        viewModel.isError.observe(this){isError->
            if (isError){
                with(binding){
                    tvError.text = getString(R.string.error, viewModel.errorMessage)
                    imgError.visibility = View.VISIBLE
                    tvError.visibility = View.VISIBLE
                    rvProfiles.visibility = View.INVISIBLE
                }
            } else{
                with(binding){
                    imgError.visibility = View.INVISIBLE
                    tvError.visibility = View.INVISIBLE
                }
                viewModel.listUsers.observe(this) { consumerUsers ->
                    setProfilesList(consumerUsers)}
            }
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showRecycleView()
    }

    private fun setProfilesList(consumerProfiles: List<ItemsItem>){
        detailAdapter.submitList(consumerProfiles)
    }

    private fun showRecycleView(){
        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvProfiles.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvProfiles.addItemDecoration(itemDecoration)
        detailAdapter = ProfileListAdapter()
        binding.rvProfiles.adapter = detailAdapter
        detailAdapter.setOnItemClickCallback(object: ProfileListAdapter.OnItemClickCallBack {
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
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
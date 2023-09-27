package com.example.githubsubmission.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubsubmission.R
import com.example.githubsubmission.database.FavoriteUser
import com.example.githubsubmission.databinding.ActivityDetailUserBinding
import com.example.githubsubmission.helper.ViewModelFactory
import com.example.githubsubmission.ui.favorite.FavoriteUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    private val favoriteViewModel by viewModels<FavoriteUserViewModel> (){
        ViewModelFactory.getInstance(application)
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.first_tab,
            R.string.second_tab
        )
        var passedName = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        passedName = intent.getStringExtra("username").toString()

        val viewModel = ViewModelProvider(this,
            DetailViewModel.DetailViewModelFactory(passedName)
        )[DetailViewModel::class.java]

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout =binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 5f

        viewModel.isLoading.observe(this) {isLoading->
            showLoading(isLoading)
        }

        viewModel.isError.observe(this){isError->
            if (isError){
                with(binding){
                    tvError.text = getString(R.string.error, viewModel.errorMessage)
                    imgError.visibility = View.VISIBLE
                    tvError.visibility = View.VISIBLE
                    cardView.visibility = View.INVISIBLE
                    tabs.visibility = View.INVISIBLE
                    viewPager.visibility = View.INVISIBLE
                }
            } else{
                with(binding){
                    imgError.visibility = View.INVISIBLE
                    tvError.visibility = View.INVISIBLE
                }
                viewModel.followCount.observe(this){pair->
                    val (followingCount, followersCount) = pair
                    binding.tvFollowingCount.text = "Following $followingCount"
                    binding.tvFollowerCount.text = "Following $followersCount"
                }
            }
        }

        viewModel.imageUrl.observe(this){imageUrl->
            viewModel.realName.observe(this){realName->
                setUserData(imageUrl, realName)
            }
        }

        with(binding){
            val favorite = FavoriteUser(username = viewModel.username, avatarUrl = viewModel.imageUrl.value)
            favoriteViewModel.getFavoriteUser(username = viewModel.username).observe(this@DetailUserActivity){FavoriteUser->
                if (isFavorite(viewModel.username) != null) {
                    floatingButton.setImageResource(R.drawable.star_24)
                    floatingButton.setOnClickListener {
                        favoriteViewModel.delete(favorite)
                    }
                } else {
                    floatingButton.setImageResource(R.drawable.outline_star_24)
                    floatingButton.setOnClickListener {
                        favoriteViewModel.insert(favorite)
                    }
                }
            }
        }
    }

    private fun setUserData(imageUrl:String, realName:String){
        binding.tvName.text = passedName
        binding.tvRealName.text = realName
        Glide.with(binding.root).load(imageUrl).into(binding.detailedImage)
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isFavorite(username:String):String?{
        return favoriteViewModel.getFavoriteUser(username).value?.username
    }

}


package com.example.githubsubmission.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubsubmission.R
import com.example.githubsubmission.database.FavoriteUser
import com.example.githubsubmission.databinding.ActivityDetailUserBinding
import com.example.githubsubmission.helper.ViewModelFactory
import com.example.githubsubmission.ui.adapter.SectionPagerAdapter
import com.example.githubsubmission.ui.favorite.FavoriteUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {


    private var _detailActivityBinding: ActivityDetailUserBinding? = null
    private val binding get() = _detailActivityBinding

    private val favoriteViewModel by viewModels<FavoriteUserViewModel> (){
        ViewModelFactory.getInstance(application)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.first_tab,
            R.string.second_tab
        )
        var passedName = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _detailActivityBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        passedName = intent.getStringExtra("username").toString()

        val viewModel = ViewModelProvider(
            this,
            DetailViewModel.DetailViewModelFactory(passedName)
        )[DetailViewModel::class.java]

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2? = binding?.viewPager
        viewPager?.adapter = sectionsPagerAdapter
        val tabs: TabLayout? = binding?.tabs
        tabs?.let {
            viewPager?.let { viewPager ->
                TabLayoutMediator(it, viewPager) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()
            }
        }
        supportActionBar?.elevation = 5f

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.isError.observe(this) { isError ->
            binding?.let { bind ->
                with(bind) {
                    if (isError) {
                        tvError.text = getString(R.string.error, viewModel.errorMessage)
                        imgError.visibility = View.VISIBLE
                        tvError.visibility = View.VISIBLE
                        cardView.visibility = View.INVISIBLE
                        tabs?.visibility = View.INVISIBLE
                        viewPager?.visibility = View.INVISIBLE
                        floatingButton.visibility = View.INVISIBLE
                    } else {
                        imgError.visibility = View.INVISIBLE
                        tvError.visibility = View.INVISIBLE
                        viewModel.followCount.observe(this@DetailUserActivity) { pair ->
                            val (followingCount, followersCount) = pair
                            tvFollowingCount.text = "Following $followingCount"
                            tvFollowerCount.text = "Following $followersCount"
                        }
                    }
                }
            }
        }

        binding?.let { bind ->
            with(bind) {
                viewModel.imageUrl.observe(this@DetailUserActivity) { imageUrl ->
                    viewModel.realName.observe(this@DetailUserActivity) { realName ->
                        setUserData(imageUrl, realName)
                        val favoriteUserInsert = FavoriteUser(passedName, imageUrl, viewModel.detailedId)
                        favoriteViewModel.getFavoriteUser(username = passedName)
                            .observe(this@DetailUserActivity) { favoriteUser ->
                                if (favoriteUser != null)
                                    floatingButton.setImageResource(R.drawable.star_24)
                                floatingButton.setOnClickListener {
                                    if (favoriteUser == null) {
                                        favoriteViewModel.insert(favoriteUserInsert)
                                        floatingButton.setImageResource(R.drawable.star_24)
                                        showToast("User has been added to favorites.")
                                    } else {
                                        favoriteViewModel.delete(favoriteUser)
                                        floatingButton.setImageResource(R.drawable.outline_star_24)
                                        showToast("User has been deleted from favorites.")
                                    }
                                }
                            }


                    }
                }
            }

        }
    }


    private fun setUserData(imageUrl: String, realName: String) {
        binding?.tvName?.text = passedName
        binding?.tvRealName?.text = realName
        binding?.let { bind -> Glide.with(bind.root).load(imageUrl).into(bind.detailedImage) }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding?.cardView?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding?.detailedImage?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding?.tabs?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding?.floatingButton?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _detailActivityBinding = null
    }

}


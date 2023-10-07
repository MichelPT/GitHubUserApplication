package com.example.githubsubmission.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsubmission.data.response.ItemsItem
import com.example.githubsubmission.databinding.FragmentFollowBinding
import com.example.githubsubmission.ui.adapter.ProfileListAdapter


class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var detailAdapter: ProfileListAdapter

    private var position: Int = 1
    private var username: String? = "username"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        Log.d("Fragment Act", "Position: $position, Username: $username")

        viewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvProfiles.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvProfiles.addItemDecoration(itemDecoration)
        detailAdapter = ProfileListAdapter()
        binding.rvProfiles.adapter = detailAdapter

        if (position == 1) {
            viewModel.followingUser.observe(viewLifecycleOwner) { consumerProfiles ->
                setProfilesList(consumerProfiles)
            }
        } else {
            viewModel.followerUser.observe(viewLifecycleOwner) { consumerProfiles ->
                setProfilesList(consumerProfiles)
            }
        }
    }

    private fun setProfilesList(consumerProfiles: List<ItemsItem>) {
        detailAdapter.submitList(consumerProfiles)
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}
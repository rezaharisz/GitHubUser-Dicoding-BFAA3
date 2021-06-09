package com.rezaharis.githubfavorites.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezaharis.githubfavorites.ui.adapter.FollowingAdapter
import com.rezaharis.githubfavorites.databinding.FragmentFollowingBinding
import com.rezaharis.githubfavorites.entity.Following
import com.rezaharis.githubfavorites.ui.viewModel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private val listFollowing = ArrayList<Following>()
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: FollowingAdapter

    companion object{
        const val ARG_USERNAME = "username"

        val TAG: String = FollowingFragment::class.java.simpleName

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter(mutableListOf<Following>() as ArrayList<Following>)
        getFollowingViewModel()
    }

    private fun getFollowingViewModel(){
        val username = arguments?.getString(ARG_USERNAME)
        followingViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        followingViewModel.getUserFollowing(username!!, requireActivity())

        followingViewModel.getFollowingModel().observe(requireActivity(),{listFollowing ->
            getAdapter()
            if (listFollowing != null){
                adapter.setData(listFollowing)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.shimmerViewContainer.visibility = View.VISIBLE
        }

        else{
            binding.shimmerViewContainer.visibility = View.GONE
        }
    }

    private fun getAdapter(){
        binding.rvView.layoutManager = LinearLayoutManager(activity)
        binding.rvView.setHasFixedSize(true)
        binding.rvView.adapter = adapter
        adapter = FollowingAdapter(listFollowing)
        adapter.notifyDataSetChanged()
        binding.rvView.canScrollVertically(1) || binding.rvView.canScrollVertically(-1)
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerViewContainer.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerViewContainer.stopShimmer()
    }

}
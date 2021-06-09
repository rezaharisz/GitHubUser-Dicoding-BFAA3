package com.rezaharis.githubuserapp2.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezaharis.githubuserapp2.ui.adapter.FollowersAdapter
import com.rezaharis.githubuserapp2.databinding.FragmentFollowersBinding
import com.rezaharis.githubuserapp2.entity.Followers
import com.rezaharis.githubuserapp2.ui.viewModel.FollowersViewModel

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private val listFollowers = ArrayList<Followers>()
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: FollowersAdapter

    companion object{
        const val ARG_USERNAME = "username"

        val TAG: String = FollowingFragment::class.java.simpleName

        fun newInstance(username: String?): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowersAdapter(mutableListOf<Followers>() as ArrayList<Followers>)
        getFollowersViewModel()
    }

    private fun getFollowersViewModel(){
        val username = arguments?.getString(ARG_USERNAME)
        followersViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        followersViewModel.getUserFollowers(username!!, requireActivity())

        followersViewModel.getFollowersModel().observe(requireActivity(),{listFollowers ->
            getAdapter()
            if (listFollowers != null){
                adapter.setData(listFollowers)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean){
        if(state){
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
        adapter = FollowersAdapter(listFollowers)
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
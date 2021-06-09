package com.rezaharis.githubuserapp2.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezaharis.githubuserapp2.ui.viewModel.MainViewModel
import com.rezaharis.githubuserapp2.ui.adapter.UserAdapter
import com.rezaharis.githubuserapp2.databinding.FragmentHomeBinding
import com.rezaharis.githubuserapp2.entity.User

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val listUsers = ArrayList<User>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    companion object{
        val TAG: String = HomeFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        mainViewModel.getUserData(requireActivity())
        searchUser()

        adapter = UserAdapter(mutableListOf<User>() as ArrayList<User>)

        mainViewModel.getMainViewModel().observe(requireActivity(),{ listUser ->
            getAdapter()
            if (listUser != null){
                adapter.setData(listUser)
                showLoading(false)
            }
        })
    }

    private fun getAdapter(){
        binding.rvView.layoutManager = LinearLayoutManager(activity)
        binding.rvView.setHasFixedSize(true)
        binding.rvView.adapter = adapter
        adapter = UserAdapter(listUsers)
        adapter.notifyDataSetChanged()
        binding.rvView.canScrollVertically(1) || binding.rvView.canScrollVertically(-1)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.shimmerViewContainer.visibility = View.VISIBLE
        }

        else{
            binding.shimmerViewContainer.visibility = View.GONE
        }
    }

    private fun searchUser(){
        binding.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                listUsers.clear()
                mainViewModel.getUserSearch(newText, activity!!)

                return true
            }
        })
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
package com.rezaharis.githubuserapp2.ui.view.fragment

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezaharis.githubuserapp2.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.rezaharis.githubuserapp2.ui.adapter.FavoriteAdapter
import com.rezaharis.githubuserapp2.database.helper.MappingHelper
import com.rezaharis.githubuserapp2.databinding.FragmentFavoritesBinding
import com.rezaharis.githubuserapp2.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavoriteAdapter

    companion object{
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAdapter()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoriteAsync()
            }
        }

        activity?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null){
            loadFavoriteAsync()
        } else{
            val listFavorite = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (listFavorite != null){
                adapter.user = listFavorite
            }
        }
    }

    private fun loadFavoriteAsync(){
        GlobalScope.launch(Dispatchers.Main){
            val deferredFavorite = async(Dispatchers.IO){
                val cursor = activity?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favorite = deferredFavorite.await()
            if (favorite.size > 0){
                adapter.user = favorite
            } else{
                adapter.user = ArrayList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.user)
    }

    private fun getAdapter(){
        binding.rvView.layoutManager = LinearLayoutManager(activity)
        binding.rvView.setHasFixedSize(true)
        adapter = FavoriteAdapter(requireActivity())
        binding.rvView.adapter = adapter
    }
}
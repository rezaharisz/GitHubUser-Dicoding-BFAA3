package com.rezaharis.githubfavorites.ui.view.activity

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rezaharis.githubfavorites.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.rezaharis.githubfavorites.database.helper.MappingHelper
import com.rezaharis.githubfavorites.databinding.ActivityMainBinding
import com.rezaharis.githubfavorites.entity.User
import com.rezaharis.githubfavorites.ui.adapter.FavoriteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FavoriteAdapter

    companion object{
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "GitHub Favorites"

        getAdapter()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoriteAsync()
            }
        }

        contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

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
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
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
        binding.rvView.layoutManager = LinearLayoutManager(this)
        binding.rvView.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvView.adapter = adapter
    }
}
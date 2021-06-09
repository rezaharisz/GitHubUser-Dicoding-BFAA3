package com.rezaharis.githubuserapp2.ui.view.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rezaharis.githubuserapp2.R
import com.rezaharis.githubuserapp2.ui.adapter.SectionsPagerAdapter
import com.rezaharis.githubuserapp2.database.DatabaseContract
import com.rezaharis.githubuserapp2.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.rezaharis.githubuserapp2.database.helper.FavoriteHelper
import com.rezaharis.githubuserapp2.databinding.ActivityUserDetailBinding
import com.rezaharis.githubuserapp2.entity.User
import com.rezaharis.githubuserapp2.ui.viewModel.MainViewModel
import java.lang.StringBuilder

class UserDetail : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private var position: Int = 0
    private lateinit var favoriteHelper: FavoriteHelper
    private var user = User()
    private var checkFavorite = false

    companion object{
        const val USER = "user"
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_FAVORITES = "extra_favorites"
        const val RESULT_ADD = 101

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayShowHomeEnabled(true)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val loginCheck = intent.getParcelableExtra<User>(USER) as User
        val cursor: Cursor = favoriteHelper.queryByLogin(loginCheck.login)
        if (cursor.moveToNext()){
            checkFavorite = true
            setStatusFavorite(true)
        }

        getData()
        getDatabase()
    }

    private fun getDatabase(){

        user = intent.getParcelableExtra<User>(USER) as User
        position = intent.getIntExtra(EXTRA_POSITION, 0)

        binding.btnFav.setOnClickListener {
            val intent = Intent()
            intent.putExtra(EXTRA_FAVORITES, user)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseContract.UserColumns.LOGIN, user.login)
            values.put(DatabaseContract.UserColumns.AVATAR_URL, user.avatar_url)
            values.put(DatabaseContract.UserColumns.NAME, user.name)
            values.put(DatabaseContract.UserColumns.COMPANY, user.company)
            values.put(DatabaseContract.UserColumns.LOCATION, user.location)
            values.put(DatabaseContract.UserColumns.REPO, user.repo)
            values.put(DatabaseContract.UserColumns.FOLLOWERS, user.followers)
            values.put(DatabaseContract.UserColumns.FOLLOWING, user.following)

            checkFavorite = !checkFavorite

            if (checkFavorite){
                contentResolver.insert(CONTENT_URI, values)
                setResult(RESULT_ADD, intent)
                setStatusFavorite(true)
                Toast.makeText(applicationContext, getString(R.string.add_data), Toast.LENGTH_SHORT).show()
            } else{
                favoriteHelper.deleteById(user.login)
                setStatusFavorite(false)
                Toast.makeText(applicationContext, getString(R.string.delete_data), Toast.LENGTH_SHORT).show()
            }
            binding.btnFav.isChecked = checkFavorite

        }

    }

    private fun setStatusFavorite(statusFavorites: Boolean) {
        if (statusFavorites){
            binding.btnFav.setBackgroundResource(R.drawable.ic_favorites)
        }
        else{
            binding.btnFav.setBackgroundResource(R.drawable.ic_no_favorite)
        }
    }

    private fun getData(){
        val user = intent.getParcelableExtra<User>(USER) as User
        Glide.with(this)
            .load(user.avatar_url)
            .override(150,150)
            .into(binding.avatarImage)
        binding.tvName.text = user.name
        binding.tvUsername.text = StringBuilder("@").append(user.login)
        binding.tvCompany.text = user.company
        binding.tvLocation.text = user.location
        binding.tvRepository.text = user.repo
        binding.tvFollowers.text = user.followers
        binding.tvFollowing.text = user.following

        getPagerAdapter(user)
    }

    private fun getPagerAdapter(user: User){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user.login
        val viewPager2: ViewPager2 = findViewById(R.id.view_pager2)
        viewPager2.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager2){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}
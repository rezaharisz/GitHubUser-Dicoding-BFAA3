package com.rezaharis.githubfavorites.ui.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rezaharis.githubfavorites.entity.Followers
import com.rezaharis.githubfavorites.ui.view.fragment.FollowersFragment
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowersViewModel: ViewModel(){
    val listFollowersMutable = MutableLiveData<ArrayList<Followers>>()

    fun getUserFollowers(username: String, context: Context){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}/followers"
//        client.addHeader("Authorization", "token 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")
//        IF TOKEN EXPIRED USE YOUR OWN TOKEN
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(FollowersFragment.TAG, result)

                try {
                    val jsonArray = JSONArray(result)

                    for (position in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(position)
                        val followersUrl = jsonObject.getString("url")

                        getUserDetail(followersUrl, context)
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error!!.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getUserDetail(followersUrl: String, context: Context){
        val listFollowers = ArrayList<Followers>()
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(followersUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                Log.d(FollowersFragment.TAG, result)

                try {
                    val jsonObject = JSONObject(result)

                    val followers = Followers()
                    followers.login = jsonObject.getString("login")
                    followers.avatar_url = jsonObject.getString("avatar_url")
                    followers.name = jsonObject.getString("name")
                    followers.company = jsonObject.getString("company")

                    listFollowers.add(followers)
                    listFollowersMutable.postValue(listFollowers)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error!!.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getFollowersModel(): LiveData<ArrayList<Followers>>{
        return listFollowersMutable
    }
}
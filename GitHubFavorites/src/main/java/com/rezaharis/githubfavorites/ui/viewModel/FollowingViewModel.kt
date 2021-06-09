package com.rezaharis.githubfavorites.ui.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rezaharis.githubfavorites.entity.Following
import com.rezaharis.githubfavorites.ui.view.fragment.FollowingFragment
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingViewModel: ViewModel() {
    val listFollowingMutable = MutableLiveData<ArrayList<Following>>()

    fun getUserFollowing(username:String, context: Context){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}/following"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(FollowingFragment.TAG, result)

                try {
                    val jsonArray = JSONArray(result)
                    for (position in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(position)
                        val followingUrl = jsonObject.getString("url")
                        getUserDetail(followingUrl, context)
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

    fun getUserDetail(followingUrl: String, context: Context){
        val listFollowing = ArrayList<Following>()
        val client = AsyncHttpClient()
//        client.addHeader("Authorization", "token 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")
//        IF TOKEN EXPIRED USE YOUR OWN TOKEN
        client.addHeader("User-Agent", "request")

        client.get(followingUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                Log.d(FollowingFragment.TAG, result)

                try {
                    val jsonObject = JSONObject(result)

                    val following = Following()
                    following.login = jsonObject.getString("login")
                    following.avatar_url = jsonObject.getString("avatar_url")
                    following.name = jsonObject.getString("name")
                    following.company = jsonObject.getString("company")

                    listFollowing.add(following)
                    listFollowingMutable.postValue(listFollowing)
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

    fun getFollowingModel(): LiveData<ArrayList<Following>>{
        return listFollowingMutable
    }
}
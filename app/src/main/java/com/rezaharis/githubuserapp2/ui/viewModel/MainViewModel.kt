package com.rezaharis.githubuserapp2.ui.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rezaharis.githubuserapp2.entity.User
import com.rezaharis.githubuserapp2.ui.view.fragment.HomeFragment
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel(){

    val listUserMutable = MutableLiveData<ArrayList<User>>()

    fun getUserData(context: Context){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(HomeFragment.TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (position in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(position)
                        val username = jsonObject.getString("login")

                        getUserDetail(username, context)
                    }
                }

                catch (e: Exception) {
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

    fun getUserDetail(username:String, context: Context){
        val listUser = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}"
//        client.addHeader("Authorization", "token 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")
//        IF TOKEN EXPIRED USE YOUR OWN TOKEN
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(HomeFragment.TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val user = User()
                    user.login = jsonObject.getString("login")
                    user.avatar_url = jsonObject.getString("avatar_url")
                    user.name = jsonObject.getString("name")
                    user.company = jsonObject.getString("company")
                    user.location = jsonObject.getString("location")
                    user.repo = jsonObject.getString("public_repos")
                    user.followers = jsonObject.getString("followers")
                    user.following = jsonObject.getString("following")

                    listUser.add(user)
                    listUserMutable.postValue(listUser)
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

    fun getUserSearch(query: String, context: Context){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=${query}"
//        client.addHeader("Authorization", "token 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")
//        IF TOKEN EXPIRED USE YOUR OWN TOKEN
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                Log.d(HomeFragment.TAG, result)

                try {
                    val jsonArray = JSONObject(result)
                    val items = jsonArray.getJSONArray("items")
                    for (position in 0 until items.length()){
                        val jsonObject = items.getJSONObject(position)
                        val login = jsonObject.getString("login")
                        getUserDetail(login, context)
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

    fun getMainViewModel(): LiveData<ArrayList<User>>{
        return listUserMutable
    }
}
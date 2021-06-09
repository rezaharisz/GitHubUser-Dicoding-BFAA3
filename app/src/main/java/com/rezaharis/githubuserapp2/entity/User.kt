package com.rezaharis.githubuserapp2.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var login:String = "",
    var avatar_url: String = "",
    var name:String = "",
    var company:String = "",
    var location:String = "",
    var repo:String = "",
    var followers:String = "",
    var following:String = ""
):Parcelable
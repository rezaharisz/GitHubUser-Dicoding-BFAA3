package com.rezaharis.githubfavorites.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Following(
    var login:String = "",
    var avatar_url: String = "",
    var name:String = "",
    var company:String = ""
):Parcelable
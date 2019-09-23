package com.wallyphoto.model

import com.google.gson.annotations.SerializedName

data class ResponsePhoto(
    @SerializedName("results") val photoList : List<Photo>?, val total : Int, val total_pages : Int)

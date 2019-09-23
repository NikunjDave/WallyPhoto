package com.wallyphoto.model

object DataHolder {

    var photoList: List<Photo>? = null
    fun saveData(list: List<Photo>) {
        photoList = list
    }
    fun getData() : List<Photo>?{
        return photoList
    }
}
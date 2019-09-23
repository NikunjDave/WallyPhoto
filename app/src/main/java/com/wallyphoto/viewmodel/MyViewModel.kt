package com.wallyphoto.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wallyphoto.Utils.AppConstant
import com.wallyphoto.repository.PhotoRepository

class MyViewModel(private val repository: PhotoRepository) {


    private val categoryName = MutableLiveData<String>()
    private val repoResult = Transformations.map(categoryName) {
        repository.photos(it, AppConstant.PAGE_SIZE)
    }

    val posts = Transformations.switchMap(repoResult, { it.pagedList })!!

    val networkState = Transformations.switchMap(repoResult, { it.networkState })


    fun showPhotos(subreddit: String): Boolean {
        if (categoryName.value == subreddit) {
            return false
        }
        categoryName.value = subreddit
        return true
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }


}
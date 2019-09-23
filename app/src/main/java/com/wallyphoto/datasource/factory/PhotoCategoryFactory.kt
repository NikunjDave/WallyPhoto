package com.wallyphoto.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.wallyphoto.datasource.PhotoCategoryDataSource
import com.wallyphoto.model.Photo
import com.wallyphoto.rest.RestApi
import java.util.concurrent.Executor

class PhotoCategoryFactory(private val categoryName : String,private val restApi : RestApi, private val retryExecutor: Executor):
    DataSource.Factory<Long, Photo>() {

    val sourceLiveData = MutableLiveData<PhotoCategoryDataSource>()
    override fun create(): DataSource<Long, Photo> {
        val dataSource = PhotoCategoryDataSource(restApi,retryExecutor,categoryName)
        sourceLiveData.postValue(dataSource)
        return dataSource
    }
}
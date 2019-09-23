package com.wallyphoto.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.wallyphoto.Utils.AppConstant
import com.wallyphoto.model.Photo
import com.wallyphoto.model.ResponsePhoto
import com.wallyphoto.repository.NetworkState
import com.wallyphoto.rest.RestApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class PhotoCategoryDataSource(private val restApi: RestApi, private val retryExecutor: Executor,private  var  category: String
) : PageKeyedDataSource<Long, Photo>() {

    val TAG = "PhotoDataSource"

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null
    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Photo>) {

        networkState.postValue(NetworkState.LOADING)
        restApi.fetchPhotoByCategory(AppConstant.CLIENT_ID,1,params.requestedLoadSize,category)
            .enqueue(object  : Callback<ResponsePhoto>{
                override fun onFailure(call: Call<ResponsePhoto>, t: Throwable) {
                    val errorMessage = t.message ?: "unknown error"
                    networkState.postValue(NetworkState.error(errorMessage))
                }

                override fun onResponse(call: Call<ResponsePhoto>, response: Response<ResponsePhoto>) {
                    if (response.isSuccessful) {

                        retry = null
                        response.body()?.let { callback.onResult(it.photoList!!, null, 2) }
                        networkState.postValue(NetworkState.LOADED)

                    } else {
                        retry = {
                            loadInitial(params, callback)
                        }

                        networkState.postValue(NetworkState.error(response.message()))
                    }
                }

            })

    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Photo>) {

        networkState.postValue(NetworkState.LOADED)

        restApi.fetchPhotoByCategory(AppConstant.CLIENT_ID,params.key,params.requestedLoadSize,category)
            .enqueue(object : Callback<ResponsePhoto>{
                override fun onResponse(call: Call<ResponsePhoto>, response: Response<ResponsePhoto>) {
                    if (response.isSuccessful) {
                        // clear retry since last request succeeded
                        retry = null

                        val nextKey = params.key + 1
                        response.body()?.let { callback.onResult(it.photoList!!, nextKey) }
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(NetworkState.error(response.message()))
                    }

                }
                override fun onFailure(call: Call<ResponsePhoto>, t: Throwable) {
                    val errorMessage = t.message ?: "unknown error"
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error(errorMessage))
                }

            })

    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Photo>) {
        // ignored, since we only ever append to our initial load
    }


}
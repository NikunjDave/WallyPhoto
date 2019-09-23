package com.wallyphoto.repository

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wallyphoto.datasource.factory.PhotoCategoryFactory
import com.wallyphoto.model.Photo
import com.wallyphoto.rest.RestApi
import java.util.concurrent.Executor

/**
 * Repository implementation that returns a Listing that loads data directly from network by using
 * the previous / next page keys returned in the query.
 */
class PhotoByPageKeyRepository(private val restApi: RestApi,
                               private val networkExecutor: Executor
) : PhotoRepository {
    override fun photos(category : String, pageSize: Int): Listing<Photo> {
        val sourceFactory = PhotoCategoryFactory(category,restApi,networkExecutor)

        val pageListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(pageSize * 2)
            .setPageSize(pageSize).build()

     val livePagedList_ = LivePagedListBuilder(sourceFactory, pageListConfig)
         .setFetchExecutor(networkExecutor).build()

        return Listing(
            pagedList = livePagedList_,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            }
        )

    }

}

package com.wallyphoto.repository

import android.app.Application
import android.content.Context
import com.wallyphoto.rest.RestApi
import com.wallyphoto.rest.RestApiFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface RepositoryProvider {

    companion object {
        private var instance: DefaultRepositoryProvider? = null
        fun instance(context: Context): RepositoryProvider {
                if (instance == null) {
                    instance = DefaultRepositoryProvider(
                        app = context.applicationContext as Application,
                        useInMemoryDb = false)
                }
                return instance!!
            }
    }

    fun getRepository(): PhotoRepository

    fun getNetworkExecutor(): Executor


    fun getRestApi(): RestApi

}
/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultRepositoryProvider(val app: Application, val useInMemoryDb: Boolean) : RepositoryProvider {

    // thread pool used for network requests
    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)


    private val api by lazy {
        RestApiFactory.create()

    }

    override fun getRepository(): PhotoRepository {

        return PhotoByPageKeyRepository(getRestApi(),getNetworkExecutor())
    }

    override fun getNetworkExecutor(): Executor {
        return NETWORK_IO
    }

    override fun getRestApi(): RestApi {
        return api
    }
}

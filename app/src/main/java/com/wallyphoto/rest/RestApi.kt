package com.wallyphoto.rest

import com.wallyphoto.model.Photo
import com.wallyphoto.model.ResponsePhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RestApi {


    @Headers("user-Accept-Version: v1")
    @GET("/photos")
    fun fetchPhotoList(
        @Query("client_id") clientID: String, @Query("page") page: Long,
        @Query("per_page") per_page: Int
    ): Call<List<Photo>>


    @Headers("user-Accept-Version: v1")
    @GET("/search/photos")
    fun fetchPhotoByCategory(
        @Query("client_id") clientID: String, @Query("page") page: Long,
        @Query("per_page") per_page: Int, @Query("query") search: String
    ): Call<ResponsePhoto>
}


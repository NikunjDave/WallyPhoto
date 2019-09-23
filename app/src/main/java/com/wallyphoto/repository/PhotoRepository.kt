package com.wallyphoto.repository

import com.wallyphoto.model.Photo

interface PhotoRepository {
    fun photos(category: String,pageSize: Int): Listing<Photo>
}
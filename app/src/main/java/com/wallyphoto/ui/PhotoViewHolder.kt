package com.wallyphoto.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.wallyphoto.R
import com.wallyphoto.model.Photo

class PhotoViewHolder (view: View, private val glide: RequestManager)
    : RecyclerView.ViewHolder(view), View.OnClickListener{
    override fun onClick(p0: View?) {

    }

    private val constraintSet = ConstraintSet()
        private  val ivThumbnail : ImageView = view.findViewById(R.id.ivThumb)
        private val rootLayout : ConstraintLayout = view.findViewById(R.id.parentConstraint)


        fun bindData(photo: Photo) {
            glide.applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.empty_photo))
                .load(photo.urls?.thumb)
                .thumbnail(0.1f)
                .into(ivThumbnail)

            val ratio =String.format("%d:%d", photo.width.toInt(),photo.height.toInt())
            constraintSet.clone(rootLayout)
            constraintSet.setDimensionRatio(ivThumbnail.id, ratio)
            constraintSet.applyTo(rootLayout)


        }

        companion object {
        fun create(parent: ViewGroup, glide: RequestManager): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false)
            return PhotoViewHolder(view, glide)
        }
    }

}
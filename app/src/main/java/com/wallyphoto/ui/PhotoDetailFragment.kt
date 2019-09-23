package com.wallyphoto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.wallyphoto.R
import com.wallyphoto.model.Photo
import kotlinx.android.synthetic.main.fragment_photo_detail.*


class PhotoDetailFragment : Fragment() {


    companion object {
        const val EXTRA_IMAGE = "extra_image"
        fun newInstance(photo : Photo?) : PhotoDetailFragment{
            var photoDetailFragment = PhotoDetailFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_IMAGE,photo)
            photoDetailFragment.arguments = args
            return photoDetailFragment
        }
    }

    private lateinit var mPhotoData : Photo
    private  val constraintSet : ConstraintSet = ConstraintSet()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPhotoData = arguments?.getParcelable(EXTRA_IMAGE)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return  inflater.inflate(R.layout.fragment_photo_detail,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(context!!)
            .applyDefaultRequestOptions(RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.empty_photo))
            .load(mPhotoData.urls?.full)
            .thumbnail(0.1f)
            .into(imageViewFull)


        val ratio =String.format("%d:%d", mPhotoData.width.toInt(),mPhotoData.height.toInt())
        constraintSet.clone(parentConstraintDetail)
        constraintSet.setDimensionRatio(imageViewFull.id, ratio)
        constraintSet.applyTo(parentConstraintDetail)

        // set meta data
        val metaData = mPhotoData?.alt_description +" \nUser : "+ mPhotoData?.user?.name + "\nLocation : "+mPhotoData?.user?.location
        tvMetaData.text = metaData

    }
}
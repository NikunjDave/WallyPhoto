package com.wallyphoto.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.wallyphoto.R
import com.wallyphoto.model.DataHolder
import com.wallyphoto.model.Photo
import kotlinx.android.synthetic.main.photo_detail_pager.*
import java.util.*


class PhotoDetailActivity : FragmentActivity() {


    private var mPhotosList: List<Photo>? = ArrayList()
    private var mAdapter: ImagePagerAdapter? = null

    private var mSelectedPositon: Int = 0

    companion object {
        val EXTRA_IMAGE_POS = "extra_image_pos"

        fun startIntent(context: Context, position: Int): Intent {
            val intent = Intent(context, PhotoDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(EXTRA_IMAGE_POS, position)
            intent.putExtras(bundle)
            return intent

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_detail_pager)
        extractData()
        setPagerAdapter()
    }

    private fun extractData() {
        mPhotosList = DataHolder.getData()
        mSelectedPositon = intent.extras.getInt(EXTRA_IMAGE_POS, 0)
    }


    private fun setPagerAdapter() {
        mAdapter = ImagePagerAdapter(supportFragmentManager, mPhotosList?.size!!)
        viewPager.adapter = mAdapter
        viewPager.currentItem = mSelectedPositon
    }


    /*
     * The main adapter that backs the ViewPager. A subclass of FragmentStatePagerAdapter as there
     * could be a large number of items in the ViewPager and we don't want to retain them all in
     * memory at once but create/destroy them on the fly.
     */
    private inner class ImagePagerAdapter(fm: FragmentManager, private val mSize: Int) : FragmentStatePagerAdapter(
        fm,
        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

        override fun getCount(): Int {
            return mSize
        }

        override fun getItem(position: Int): Fragment {
            return PhotoDetailFragment.newInstance(mPhotosList?.get(position))
        }
    }


}



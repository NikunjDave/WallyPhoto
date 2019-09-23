package com.wallyphoto

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.wallyphoto.Utils.AppConstant
import com.wallyphoto.Utils.NetworkUtils
import com.wallyphoto.adapter.PhotoAdapter
import com.wallyphoto.model.DataHolder
import com.wallyphoto.model.Photo
import com.wallyphoto.repository.RepositoryProvider
import com.wallyphoto.ui.PhotoDetailActivity
import com.wallyphoto.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), PhotoAdapter.OnItemClickListener {

    override fun onItemClick(position: Int) {
        if (adapter.currentList != null && adapter.currentList?.size!! > 0) {
            photoList.clear()
            photoList.addAll(adapter.currentList!!)
            DataHolder.saveData(photoList)
            val intent = PhotoDetailActivity.startIntent(this, position)
            startActivity(intent)
        }
    }

    lateinit var myViewModel: MyViewModel

    var photoList = arrayListOf<Photo>()
    lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setup adapter
        setAdapter()
        // fetch photos
        loadData()
    }

    private fun setAdapter() {
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val glide = Glide.with(this)
        adapter = PhotoAdapter(glide, this) {
            myViewModel.retry()
        }

        rvPhotos.layoutManager = staggeredGridLayoutManager

        rvPhotos.adapter = adapter
    }

    private fun loadData() {

        if (NetworkUtils.isNetworkConnected(this)) {
            myViewModel = MyViewModel(RepositoryProvider.instance(this).getRepository())

            // category is static
            myViewModel.showPhotos(AppConstant.CATEGORY)

            // receive photos
            myViewModel.posts.observe(this, Observer { photoList
                ->
                adapter.submitList(photoList)
            })
            // receive status
            myViewModel.networkState.observe(this, Observer {
                adapter.setNetworkState(it)
            })
        } else {
            showEmptyScreen()
        }
    }


    private fun showEmptyScreen() {
        rvPhotos.visibility = View.GONE
        ll_no_data.visibility = View.VISIBLE
        btn_retry.setOnClickListener {
            rvPhotos.visibility = View.VISIBLE
            ll_no_data.visibility = View.GONE
            loadData()
        }
    }

}



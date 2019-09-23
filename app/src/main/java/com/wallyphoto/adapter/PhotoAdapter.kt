package com.wallyphoto.adapter

import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wallyphoto.model.Photo
import com.wallyphoto.repository.NetworkState
import com.wallyphoto.ui.NetworkStateItemViewHolder
import com.wallyphoto.ui.PhotoViewHolder


class PhotoAdapter(
    private val glide: RequestManager, private val mOnItemClickListener: OnItemClickListener,
    private val retryCallback: () -> Unit
) : PagedListAdapter<Photo, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private var networkState: NetworkState? = null


    companion object {

        val TYPE_PROGRESS = 0
        val TYPE_ITEM = 1

        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.equals(newItem)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PROGRESS) {
            NetworkStateItemViewHolder.create(parent, retryCallback)
        } else {
            PhotoViewHolder.create(parent, glide)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is PhotoViewHolder) {
            holder.bindData(getItem(position)!!)
            holder.itemView.setOnClickListener {
                mOnItemClickListener.onItemClick(holder.adapterPosition)
            }
        } else {
            (holder as NetworkStateItemViewHolder).bindTo(networkState)
        }


    }
    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED

    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_PROGRESS
        } else {
            TYPE_ITEM
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val previousExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }



    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}



package com.wallyphoto.model

import android.os.Parcel
import android.os.Parcelable

class Urls() : Parcelable{

    var raw: String? = null
    var full: String? = null
    var regular: String? = null
    var small: String? = null
    var thumb: String? = null

    constructor(parcel: Parcel) : this() {
        raw = parcel.readString()
        full = parcel.readString()
        regular = parcel.readString()
        small = parcel.readString()
        thumb = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(raw)
        parcel.writeString(full)
        parcel.writeString(regular)
        parcel.writeString(small)
        parcel.writeString(thumb)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Urls> {
        override fun createFromParcel(parcel: Parcel): Urls {
            return Urls(parcel)
        }

        override fun newArray(size: Int): Array<Urls?> {
            return arrayOfNulls(size)
        }
    }
}
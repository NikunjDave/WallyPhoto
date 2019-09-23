package com.wallyphoto.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Photo() : Parcelable{

    var id: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var width: Float = 0.toFloat()
    var height: Float = 0.toFloat()
    var color: String? = null
    var description: String? = null
    var alt_description: String? = null

    @SerializedName("urls")
    var urls: Urls? = null
    @SerializedName("user")
    var user : User ? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        created_at = parcel.readString()
        updated_at = parcel.readString()
        width = parcel.readFloat()
        height = parcel.readFloat()
        color = parcel.readString()
        description = parcel.readString()
        alt_description = parcel.readString()
        urls = parcel.readParcelable(Urls::class.java.classLoader)
        user = parcel.readParcelable(User::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeFloat(width)
        parcel.writeFloat(height)
        parcel.writeString(color)
        parcel.writeString(description)
        parcel.writeString(alt_description)
        parcel.writeParcelable(urls, flags)
        parcel.writeParcelable(user,flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(obj: Any?): Boolean {
        if (obj === this)
            return true

        val photo = obj as Photo?
        return photo?.id == this.id
    }

}


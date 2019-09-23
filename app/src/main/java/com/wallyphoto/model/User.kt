package com.wallyphoto.model

import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable{
    var id: String ? = null
    var location: String ? = null
    var name: String ? = null
    var username: String ? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        location = parcel.readString()
        name = parcel.readString()
        username = parcel.readString()
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(location)
        parcel.writeString(name)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
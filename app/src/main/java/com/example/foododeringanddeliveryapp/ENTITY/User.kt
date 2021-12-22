package com.example.foododeringanddeliveryapp.ENTITY

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey

@androidx.room.Entity
data class User(

    var _id: String? = null,

    var email: String? = null,
    var fullname: String? = null,
    var phone: String? = null,
    var password: String? = null
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        userId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(email)
        parcel.writeString(fullname)
        parcel.writeString(phone)
        parcel.writeString(password)
        parcel.writeInt(userId)
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

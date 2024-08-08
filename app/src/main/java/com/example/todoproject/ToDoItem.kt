package com.example.todoproject

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class ToDoItem(var title: String, var detail: String, var date: Date) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Date(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(detail)
        parcel.writeLong(date.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ToDoItem> {
        override fun createFromParcel(parcel: Parcel): ToDoItem {
            return ToDoItem(parcel)
        }

        override fun newArray(size: Int): Array<ToDoItem?> {
            return arrayOfNulls(size)
        }
    }
}

package com.example.todoproject.data

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

data class Schedule(
    val idx: Long,
    val title: String,
    val content: String,
    val date: String, // 서버에서 반환하는 날짜 형식에 맞춰야 합니다. (예: "yyyy-MM-dd HH:mm:ss")
    val memberIdx: Long
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(idx)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(date)
        parcel.writeLong(memberIdx)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }
    }
}

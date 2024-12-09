package com.example.demo_recycleview.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
class LoaiSanPham(
    val idLoai_sp: Int,
    val tenLoai_sp: String
) : Parcelable {
}
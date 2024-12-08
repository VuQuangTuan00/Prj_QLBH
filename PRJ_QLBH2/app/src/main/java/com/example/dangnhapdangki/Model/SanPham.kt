package com.example.demo_recycleview.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SanPham (
    val id_sanPham:Int,
    val img_sp:String,
    val ten_sp:String,
    val idLoai_sp:LoaiSanPham,
    val soLuong_sp:Int,
    val idDonVi_sp:DonVi,
    val gia_sp:Double,
    val thongTin:String,
): Parcelable {

}
package com.example.demo_recycleview.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SanPham (
    var id_sanPham:Int,
    var img_sp:String,
    var ten_sp:String,
    var idLoai_sp:LoaiSanPham,
    var soLuong_sp:Int,
    var idDonVi_sp:DonVi,
    var gia_sp:Double,
    var thongTin:String,
): Parcelable {

}
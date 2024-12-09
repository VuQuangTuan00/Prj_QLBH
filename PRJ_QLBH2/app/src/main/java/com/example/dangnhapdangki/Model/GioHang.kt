package com.example.dangnhapdangki.Model

data class GioHang(
    var maSP: String,
    var tenSP: String,
    var loaiSP: String,
    var gia: Double,
    var soLuong: Int,
    var hinh: String,
    var daChon: Int,
    var maKH: String


) {
    override fun toString(): String {
        return "GioHang(maSP='$maSP', tenSP='$tenSP', loaiSP='$loaiSP', gia=$gia, soLuong=$soLuong, maKH=$maKH)"
    }
}

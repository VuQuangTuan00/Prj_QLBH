package com.example.dangnhapdangki

data class GioHang(
        val maSP: String,
        val tenSP: String,
        val loaiSP: String,
        val gia: Double,
        val soLuong: Int,
        var hinh: String
) {
    override fun toString(): String {
        return "GioHang(maSP='$maSP', tenSP='$tenSP', loaiSP='$loaiSP', gia=$gia, soLuong=$soLuong)"
    }
}

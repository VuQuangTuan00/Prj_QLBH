package com.example.dangnhapdangki.Model

data class DonHang(
    var maSP: String,
    var tenSP: String,
    var loaiSP: String,
    var diaChi: String,
    var thoiGian: String,
    var trangThai: String,
    var gia: Double,
    var soLuong: Int,
    var hinh: String,
    var maKH: String? = null // maKH có giá trị mặc định là null
) {
    override fun toString(): String {
        return """
            DonHang(
                maSP='$maSP', 
                tenSP='$tenSP', 
                loaiSP='$loaiSP', 
                diaChi='$diaChi', 
                thoiGian='$thoiGian', 
                trangThai='$trangThai', 
                gia=$gia, 
                soLuong=$soLuong, 
                hinh='$hinh', 
                maKH=${maKH ?: "null"}
            )
        """.trimIndent()
    }
}

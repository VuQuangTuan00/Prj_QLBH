package com.example.dangnhapdangki;

public class DonHang {
    private String maSP, tenSP, loaiSP, diaChi, thoiGian, trangThai;
    private double gia;
    private int soLuong;

    private  String maKH;

    @Override
    public String toString() {
        return "DonHang{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", loaiSP='" + loaiSP + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", thoiGian='" + thoiGian + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", gia=" + gia +
                ", soLuong=" + soLuong +
                '}';
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public DonHang(String maSP, String tenSP, String loaiSP, String diaChi, String thoiGian, String trangThai, double gia, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.diaChi = diaChi;
        this.thoiGian = thoiGian;
        this.trangThai = trangThai;
        this.gia = gia;
        this.soLuong = soLuong;
    }
}

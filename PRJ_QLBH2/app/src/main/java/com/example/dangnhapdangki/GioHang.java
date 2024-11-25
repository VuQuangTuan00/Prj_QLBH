package com.example.dangnhapdangki;

public class GioHang {
    private String maSP,tenSP, loaiSP;
    private double gia;
    private int soLuong;


    public GioHang(String maSP, String tenSP, String loaiSP, double gia, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "GioHang{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", loaiSP='" + loaiSP + '\'' +
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
}


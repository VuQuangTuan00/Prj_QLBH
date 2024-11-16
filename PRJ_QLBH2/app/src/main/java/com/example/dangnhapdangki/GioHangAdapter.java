package com.example.dangnhapdangki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.prj_qlbh.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GioHangAdapter extends ArrayAdapter<GioHang> {
    private final Context context;
    private final ArrayList<GioHang> data;
    private final int resource;

    public GioHangAdapter(@NonNull Context context, int resource, ArrayList<GioHang> data) {
        super(context, resource, data);

        this.context = context;
        this.data = data;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        TextView tvTen = convertView.findViewById(R.id.tv_product_name);
        TextView tvLoaiSP  = convertView.findViewById(R.id.tv_product_category);
        TextView tvGia = convertView.findViewById(R.id.tv_product_price);
        TextView tvSoLuong = convertView.findViewById(R.id.tv_quantity_text);

        GioHang gioHang = data.get(position);

        tvTen.setText(gioHang.getTenSP());
        tvLoaiSP.setText(gioHang.getLoaiSP());
        tvGia.setText(String.valueOf(gioHang.getGia())+"đ");
        tvSoLuong.setText(String.valueOf(gioHang.getSoLuong()));
        return convertView;
    }
}

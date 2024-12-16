package com.example.dangnhapdangki.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dangnhapdangki.Activity.MainActivityGioHang;
import com.example.dangnhapdangki.ChuyenDoiHinhAnh;
import com.example.dangnhapdangki.Database.DataBaseGioHang;
import com.example.dangnhapdangki.Model.GioHang;
import com.example.prj_qlbh.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GioHangAdapter extends ArrayAdapter<GioHang> {
    private final Context context;
    private final ArrayList<GioHang> data;
    private final int resource;
    private MainActivityGioHang mainActivityGioHang;
    private String maKH;

    public GioHangAdapter(@NonNull Context context, int resource, ArrayList<GioHang> data, MainActivityGioHang mainActivityGioHang, String maKH) {
        super(context, resource, data);

        this.context = context;
        this.data = data;
        this.resource = resource;
        this.mainActivityGioHang = mainActivityGioHang;
        this.maKH = maKH;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);


        ImageView iv_product = convertView.findViewById(R.id.iv_product);
        TextView tvTen = convertView.findViewById(R.id.tv_product_name);
        TextView tvLoaiSP = convertView.findViewById(R.id.tv_product_category);
        TextView tvGia = convertView.findViewById(R.id.tv_product_price);
        TextView tvSoLuong = convertView.findViewById(R.id.tv_quantity_text);
        Button btn_decrease = convertView.findViewById(R.id.btn_decrease);
        Button btn_increase = convertView.findViewById(R.id.btn_increase);
        LinearLayout linearLayoutGioHang = convertView.findViewById(R.id.linearLayoutGioHang);

        GioHang gioHang = data.get(position);
        if (gioHang.getDaChon() == 0) {
            linearLayoutGioHang.setBackgroundResource(R.drawable.botron_giohang);
        } else {
            linearLayoutGioHang.setBackgroundResource(R.drawable.botron_giohang2);
        }

        linearLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gioHang.getDaChon() == 0) {
                    DataBaseGioHang db = new DataBaseGioHang(context);
                    db.updateDaChon(gioHang.getMaSP(), maKH, 1);
                    linearLayoutGioHang.setBackgroundResource(R.drawable.botron_giohang2);
                    gioHang.setDaChon(1);
                    notifyDataSetChanged();
                } else {
                    DataBaseGioHang db = new DataBaseGioHang(context);
                    db.updateDaChon(gioHang.getMaSP(), maKH, 0);
                    linearLayoutGioHang.setBackgroundResource(R.drawable.botron_giohang2);
                    gioHang.setDaChon(0);
                    notifyDataSetChanged();
                }

                // Cập nhật tổng tiền trong MainActivity
                if (mainActivityGioHang != null) {
                    mainActivityGioHang.tingTongTien(); // Gọi hàm tingTongTien()
                }

            }
        });


        ChuyenDoiHinhAnh chuyenDoiHinhAnh = new ChuyenDoiHinhAnh();

        btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSoLuong = gioHang.getSoLuong();
                if (currentSoLuong > 1) { // Đảm bảo số lượng không giảm xuống dưới 1
                    int newSoLuong = currentSoLuong - 1;
                    gioHang.setSoLuong(newSoLuong);

                    // Cập nhật cơ sở dữ liệu
                    DataBaseGioHang db = new DataBaseGioHang(context);
                    db.updateSoLuong(gioHang.getMaSP(),maKH, newSoLuong);

                    // Cập nhật lại giao diện
                    notifyDataSetChanged();

                    // Cập nhật tổng tiền trong MainActivity
                    if (mainActivityGioHang != null) {
                        mainActivityGioHang.tingTongTien(); // Gọi hàm tingTongTien()
                    }
                }
            }
        });

        btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSoLuong = gioHang.getSoLuong();
                int newSoLuong = currentSoLuong + 1;
                gioHang.setSoLuong(newSoLuong);

                // Cập nhật cơ sở dữ liệu
                DataBaseGioHang db = new DataBaseGioHang(context);
                db.updateSoLuong(gioHang.getMaSP(),maKH, newSoLuong);

                // Cập nhật lại giao diện
                notifyDataSetChanged();

                // Cập nhật tổng tiền trong MainActivity
                if (mainActivityGioHang != null) {
                    mainActivityGioHang.tingTongTien(); // Gọi hàm tingTongTien()
                }
            }
        });


        try {
            if (gioHang.getHinh().isEmpty() || gioHang.getHinh() == null || gioHang.getHinh().equals("") || gioHang.getHinh().equals(null)) {
                iv_product.setImageResource(R.drawable.img_null_sanpham);
            } else {
//                StringBase64 stringBase64 = new StringBase64();
                byte[] bytes = chuyenDoiHinhAnh.chuyenStringSangByte(gioHang.getHinh(),context);
                Bitmap bitmap = chuyenDoiHinhAnh.chuyenByteSangBitMap(bytes,context);
                iv_product.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            iv_product.setImageResource(R.drawable.img_null_sanpham);
        }


        try {
            double totalPrice = gioHang.getGia();

            // Kiểm tra nếu totalPrice là số nguyên
            String formattedPrice;
            if (totalPrice % 1 == 0) {
                // Nếu không có phần thập phân đáng kể, chỉ định dạng phần nguyên
                DecimalFormat df = new DecimalFormat("#,###,###");
                formattedPrice = df.format(totalPrice);
            } else {
                // Giữ nguyên định dạng 2 chữ số thập phân
                DecimalFormat df = new DecimalFormat("#,###,###.00");
                formattedPrice = df.format(totalPrice);
            }

            tvGia.setText(formattedPrice + "đ");
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu cần
            e.printStackTrace();
        }



        tvTen.setText(gioHang.getTenSP());
        tvLoaiSP.setText(gioHang.getLoaiSP());

        tvSoLuong.setText(String.valueOf(gioHang.getSoLuong()));
        return convertView;
    }


}

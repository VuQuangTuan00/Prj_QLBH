package com.example.dangnhapdangki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        String base64 = "0ooqqKFST00_V9rtK6G6tVdNDbPqvS1qus9UiURSERAewY_jzrr1ixaxrVBiyQRasmiCLZo1bpgkg2QQSIkmgiQBHREgFEU08iBAMIAI9xIiL8q7oiIlKIiJSiIiUoiIlKIiJSiIiUoiIlKIiJSiIiUoiIlKIiJSiIiUoiIlKIiJSiIiUoiIlKIiJSiIiUoiIlKIiJSiIiUoiIlKIiJSiIiUoiIlKIiJSiIiUoiIlKIiJSiIiUoiIlK_9k";
        ImageView iv_product = convertView.findViewById(R.id.iv_product);
        TextView tvTen = convertView.findViewById(R.id.tv_product_name);
        TextView tvLoaiSP  = convertView.findViewById(R.id.tv_product_category);
        TextView tvGia = convertView.findViewById(R.id.tv_product_price);
        TextView tvSoLuong = convertView.findViewById(R.id.tv_quantity_text);

        GioHang gioHang = data.get(position);

        try{
         byte[] byteIV =   chuyenStringSangByte(base64);
           iv_product.setImageBitmap(chuyenByteSangBitMap(byteIV));
        }
        catch (Exception e){

        }
        tvTen.setText(gioHang.getTenSP());
        tvLoaiSP.setText(gioHang.getLoaiSP());
        tvGia.setText(String.valueOf(gioHang.getGia())+"đ");
        tvSoLuong.setText(String.valueOf(gioHang.getSoLuong()));
        return convertView;
    }

    // chuyen Byte[] Sang Chuoi
    private String chuyenByteSangChuoi(byte[] byteArray) {
        String base64String = android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE);
        return base64String;
    }

    //chuyen String Sang Byte[]
    private byte[] chuyenStringSangByte(String str) {
        byte[] byteArray = android.util.Base64.decode(str, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE);
        return byteArray;
    }

    //Chuyen byte[] sang bitMap
    private Bitmap chuyenByteSangBitMap(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

}

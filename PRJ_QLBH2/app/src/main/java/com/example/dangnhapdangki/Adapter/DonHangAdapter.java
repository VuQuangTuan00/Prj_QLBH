package com.example.dangnhapdangki.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dangnhapdangki.ChuyenDoiHinhAnh;
import com.example.dangnhapdangki.Database.DataBaseDonHang;
import com.example.dangnhapdangki.Model.DonHang;
import com.example.prj_qlbh.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DonHangAdapter extends ArrayAdapter<DonHang> {
    private final Context context;
    private final ArrayList<DonHang> data;
    private final int resource;

    private DataBaseDonHang dbDonHang;

    public DonHangAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DonHang> data,  DataBaseDonHang dbDonHang) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.resource = resource;
        this.dbDonHang = dbDonHang;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        ImageView iv_product = convertView.findViewById(R.id.iv_product);
        TextView tv_product_name = convertView.findViewById(R.id.tv_product_name);
        TextView tv_product_category = convertView.findViewById(R.id.tv_product_category);
        TextView tv_product_price = convertView.findViewById(R.id.tv_product_price);
        TextView tv_quantity_text = convertView.findViewById(R.id.tv_quantity_text);
        TextView tv_DiaChi = convertView.findViewById(R.id.tv_DiaChi);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvTrangThai = convertView.findViewById(R.id.tvTrangThai);
        TextView tvTongTien = convertView.findViewById(R.id.tvTongTien);
        LinearLayout linearLayoutDonHang = convertView.findViewById(R.id.linearLayoutDonHang);


        DonHang donHang = data.get(position);
        ChuyenDoiHinhAnh chuyenDoiHinhAnh = new ChuyenDoiHinhAnh();
        tv_product_name.setText(donHang.getTenSP());
        tv_product_category.setText(donHang.getLoaiSP());

        tv_quantity_text.setText(String.valueOf("x" + donHang.getSoLuong())); // Chuyển đổi số lượng sang chuỗi
        tv_DiaChi.setText(donHang.getDiaChi()); // Đặt giá trị cho địa chỉ
        tvDate.setText(donHang.getThoiGian()); // Hiển thị ngày đặt hàng
        tvTrangThai.setText(donHang.getTrangThai()); // Hiển thị trạng thái đơn hàng


        // Format giá tiền
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")); // Định dạng tiền Việt Nam
        String formattedPrice = currencyFormatter.format(donHang.getGia());
        String formattedTotal = currencyFormatter.format(donHang.getGia() * donHang.getSoLuong());

        // Gán giá trị đã định dạng vào TextView
        tv_product_price.setText(formattedPrice); // Hiển thị giá sản phẩm


        // Tính tổng tiền
        double totalPrice = donHang.getGia() * donHang.getSoLuong();
        String formattedTotalPrice = currencyFormatter.format(totalPrice);

        // Hiển thị tổng tiền đã định dạng
        tvTongTien.setText(formattedTotalPrice);



        try {
            if (donHang.getHinh().isEmpty() || donHang.getHinh() == null || donHang.getHinh().equals("") || donHang.getHinh().equals(null)) {
                iv_product.setImageResource(R.drawable.img_null_sanpham);
            } else {
//                StringBase64 stringBase64 = new StringBase64();
                byte[] bytes = chuyenDoiHinhAnh.chuyenStringSangByte(donHang.getHinh());
                Bitmap bitmap = chuyenDoiHinhAnh.chuyenByteSangBitMap(bytes);
                iv_product.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            iv_product.setImageResource(R.drawable.img_null_sanpham);
        }


        linearLayoutDonHang.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Xử lý sự kiện nhấn đè ở đây
                new AlertDialog.Builder(context)
                        .setTitle("Xóa đơn hàng")
                        .setMessage("Bạn có chắc chắn muốn xóa đơn hàng này?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Gọi hàm xóa đơn hàng
                                int rowsDeleted = dbDonHang.deleteDonHangByDateAndMaSPAndMaKH(donHang);
                                if (rowsDeleted > 0) {
                                    Toast.makeText(context, "Đơn hàng đã được xóa!", Toast.LENGTH_SHORT).show();
                                    // Cập nhật lại danh sách
                                    data.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();

                return true; // Trả về true để báo hiệu rằng sự kiện đã được xử lý
            }
        });

        return convertView;
    }
}


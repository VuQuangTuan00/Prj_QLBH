package com.example.dangnhapdangki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ChuyenDoiHinhAnh {
    public ChuyenDoiHinhAnh() {
    }

    // chuyen Byte[] Sang Chuoi
    public String chuyenByteSangChuoi(byte[] byteArray) {
        // Kiểm tra kích thước của byte[] trước khi mã hóa
        if (byteArray.length > 1048576) {  // 1MB là giới hạn ví dụ
            // Nếu kích thước byteArray quá lớn, trả về null hoặc thông báo lỗi
            return null;
        }

        return android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE);
    }


    public byte[] chuyenStringSangByte(String str) {
        // Kiểm tra chiều dài của chuỗi Base64 trước khi giải mã
        if (str.length() > 1048576) {  // 1MB là giới hạn ví dụ
            // Nếu chuỗi quá lớn, trả về null hoặc thông báo lỗi
            return null;
        }

        return android.util.Base64.decode(str, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE);
    }


    public Bitmap chuyenByteSangBitMap(byte[] byteArray) {
        // Kiểm tra kích thước của byte[] trước khi giải mã ảnh
        if (byteArray.length > 1048576) {  // 1MB là giới hạn ví dụ
            // Nếu ảnh quá lớn, trả về null hoặc ảnh mặc định
            return null;
        }

        // Kiểm tra kích thước ảnh trước khi giải mã
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Giới hạn kích thước ảnh
        int maxWidth = 1024;
        int maxHeight = 1024;

        if (options.outWidth > maxWidth || options.outHeight > maxHeight) {
            // Nếu ảnh quá lớn, không giải mã và trả về null
            return null;
        }

        // Giải mã ảnh nếu kích thước hợp lệ
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }
}

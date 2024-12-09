package com.example.dangnhapdangki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ChuyenDoiHinhAnh {
    public ChuyenDoiHinhAnh() {
    }

    public String chuyenByteSangChuoi(byte[] byteArray) {
        if (byteArray == null || byteArray.length == 0) {
            throw new IllegalArgumentException("Dữ liệu byte array không hợp lệ.");
        }
        if (byteArray.length > 1048576) {  // 1MB
            throw new IllegalArgumentException("Kích thước byte array vượt quá giới hạn cho phép.");
        }

        return android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE);
    }

    public byte[] chuyenStringSangByte(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Chuỗi Base64 không hợp lệ.");
        }
        if (str.length() > 1048576) {  // 1MB
            throw new IllegalArgumentException("Chiều dài chuỗi Base64 vượt quá giới hạn cho phép.");
        }

        return android.util.Base64.decode(str, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE);
    }

    public Bitmap chuyenByteSangBitMap(byte[] byteArray) {
        if (byteArray == null || byteArray.length == 0) {
            throw new IllegalArgumentException("Dữ liệu byte array không hợp lệ.");
        }
        if (byteArray.length > 1048576) {  // 1MB
            throw new IllegalArgumentException("Kích thước byte array vượt quá giới hạn cho phép.");
        }

        // Kiểm tra kích thước ảnh trước khi giải mã
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Giới hạn kích thước ảnh
        int maxWidth = 1024;
        int maxHeight = 1024;

        if (options.outWidth > maxWidth || options.outHeight > maxHeight) {
            // Tính toán hệ số nén để giảm kích thước ảnh
            int scaleFactor = Math.max(options.outWidth / maxWidth, options.outHeight / maxHeight);
            options.inSampleSize = scaleFactor;
        }

        // Giải mã ảnh nếu kích thước hợp lệ
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

}

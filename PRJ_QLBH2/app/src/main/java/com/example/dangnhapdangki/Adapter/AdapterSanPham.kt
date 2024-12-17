import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.dangnhapdangki.Activity.ChiTietSanPham
import com.example.dangnhapdangki.Activity.DangNhap
import com.example.dangnhapdangki.Activity.DanhSachSanPham
import com.example.dangnhapdangki.Activity.MainActivityGioHang
import com.example.dangnhapdangki.Activity.TrangChu
import com.example.dangnhapdangki.Activity.TrangChu.Companion.sanPhamBanHang
import com.example.dangnhapdangki.Activity.UpdateProduct
import com.example.dangnhapdangki.ChuyenDoiHinhAnh
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.dangnhapdangki.Model.GioHang
import com.example.demo_recycleview.Model.DonVi
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.databinding.ItemSanphamBinding

class AdapterSanPham(
    private var dsSanPham: ArrayList<SanPham>,
    private val dsLoaiSP: ArrayList<LoaiSanPham>,
    private val dsDonViSP: ArrayList<DonVi>,
    private val context: Context

) {

    fun populateLinearLayout(parentLayout: ViewGroup) {
        parentLayout.removeAllViews() // Xóa các mục cũ

        val inflater = LayoutInflater.from(context)

        for (sanPham in dsSanPham) {
            val binding = ItemSanphamBinding.inflate(inflater, parentLayout, false)

            // Tìm loại sản phẩm và đơn vị
            val loaiSP = dsLoaiSP.find { it.idLoai_sp == sanPham.idLoai_sp.idLoai_sp }
            val donVi = dsDonViSP.find { it.idDonVi_sp == sanPham.idDonVi_sp.idDonVi_sp }

            // Hiển thị thông tin sản phẩm
            val giaText =
                "${sanPham.gia_sp}vnd / ${sanPham.soLuong_sp}${sanPham.idDonVi_sp.tenDonVi_sp}"
            val spannable = SpannableString(giaText)
            val indexSlash = giaText.indexOf("/")
            if (indexSlash != -1) {
                spannable.setSpan(
                    RelativeSizeSpan(0.5f),
                    indexSlash + 2,
                    giaText.length,
                    0
                )
            }

            if (TrangChu.manHinh.equals("QuanTri")) {
                binding.btnMuaSp.text = "Xóa sản phẩm"
            }
            if (loaiSP != null && donVi != null) {
                binding.tvTenSp.text = sanPham.ten_sp
                binding.tvGia.text = spannable
                binding.tvLoaiSp.text = sanPham.idLoai_sp.tenLoai_sp

                val chuyenDoiHinhAnh = ChuyenDoiHinhAnh()
                try {
                    val hinhByte = chuyenDoiHinhAnh.chuyenStringSangByte(sanPham.img_sp, context)
                    val hinhBitmap = chuyenDoiHinhAnh.chuyenByteSangBitMap(hinhByte, context)
                    binding.imgSanPham.setImageBitmap(hinhBitmap)
                } catch (e: Exception) {
                    Log.e("AdapterSanPhamGrid", "Lỗi chuyển đổi hình ảnh", e)
                }
            }

            // Sự kiện khi click vào sản phẩm
            binding.linearLayoutSanPham.setOnClickListener {
                DanhSachSanPham.sanPhamBanHang = sanPham
                DanhSachSanPham.hinhSP = sanPham.img_sp
                val intent = Intent(context, UpdateProduct::class.java)
                context.startActivity(intent)
            }

            binding.btnMuaSp.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                    .setPositiveButton("Yes") { dialog, which ->
                        val dbSanPham: SanPhamDBHelper by lazy { SanPhamDBHelper(context) }
                        dbSanPham.deleteProduct(sanPham.id_sanPham)
                        dsSanPham.clear()
                        dsSanPham.addAll(dbSanPham.getAllProducts())
                        populateLinearLayout(parentLayout)
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }

            // Thêm vào LinearLayout
            parentLayout.addView(binding.root)
        }

        // Kiểm tra số lượng sản phẩm lẻ và thêm View giả nếu cần
        if (dsSanPham.size % 2 != 0) {
            val binding = ItemSanphamBinding.inflate(inflater, parentLayout, false)

            // Ẩn nội dung của View giả
            binding.tvTenSp.visibility = View.INVISIBLE
            binding.tvGia.visibility = View.INVISIBLE
            binding.tvLoaiSp.visibility = View.INVISIBLE
            binding.imgSanPham.visibility = View.INVISIBLE
            binding.btnMuaSp.visibility = View.INVISIBLE
            binding.root.setBackgroundColor(android.graphics.Color.TRANSPARENT)



            // Thêm View giả vào bố cục
            parentLayout.addView(binding.root)
        }
    }


    // Hàm cập nhật dữ liệu
    fun updateData(newSanPhamList: ArrayList<SanPham>, parentLayout: ViewGroup) {
        dsSanPham.clear() // Xóa dữ liệu cũ
        dsSanPham.addAll(newSanPhamList) // Thêm dữ liệu mới
        populateLinearLayout(parentLayout) // Vẽ lại layout với dữ liệu mới
    }


}
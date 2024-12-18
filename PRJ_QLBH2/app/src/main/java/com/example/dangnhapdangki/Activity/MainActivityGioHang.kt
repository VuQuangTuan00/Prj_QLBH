package com.example.dangnhapdangki.Activity


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.Database.DataBaseGioHang
import com.example.dangnhapdangki.Model.GioHang
import com.example.dangnhapdangki.Adapter.GioHangAdapter
import com.example.dangnhapdangki.Database.LoaiSanPhamDBHelper
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.dangnhapdangki.StringBase64
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.Model.SanPham
import com.example.prj_qlbh.R


class MainActivityGioHang : AppCompatActivity() {
    private lateinit var lvDanhSachGioHang: ListView
    private lateinit var btnDatHang: Button
    private lateinit var imgBack: ImageView
    private lateinit var btn_del_giohang: Button
    private lateinit var tvTienHang: TextView
    private lateinit var tvTongTien: TextView
    private lateinit var tvTongDonHang: TextView
    private lateinit var tvDonHang: TextView

    private  var maKH: String = DangNhap.maKH

    private val dbGioHang: DataBaseGioHang by lazy { DataBaseGioHang(this) }
    private val dbSanPham: SanPhamDBHelper by lazy { SanPhamDBHelper(this) }
    private val dbLoaiSanPham: LoaiSanPhamDBHelper by lazy { LoaiSanPhamDBHelper(this) }
    private val dsGioHang = ArrayList<GioHang>()
    private val dsSanPham = ArrayList<SanPham>()
    private lateinit var gioHangAdapter: GioHangAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_gio_hang)
        setControl()
        setEvent()
    }

    private fun setControl() {
        lvDanhSachGioHang = findViewById(R.id.lvGioHang)
        btnDatHang = findViewById(R.id.btnDatHang)
        imgBack = findViewById(R.id.ivBack)
        btn_del_giohang = findViewById(R.id.btn_del_giohang)

        tvTienHang = findViewById(R.id.tvTienHang)
        tvTongTien = findViewById(R.id.tvTongTien)
        tvTongDonHang = findViewById(R.id.tvTongDonHang)
        tvDonHang = findViewById(R.id.tvDonHang)
    }

    private fun setEvent() {
        // Khởi tạo adapter trước khi sử dụng

        dsSanPham.clear()
        dsSanPham.addAll(dbSanPham.getAllProducts())

        for (sanPham in dsSanPham) {
            val loaiSP = dbLoaiSanPham.getTenLoaiSanPhamById(sanPham.idLoai_sp.idLoai_sp ?: 0) ?: "Không xác định"

            // Tạo đối tượng GioHang từ thông tin của SanPham
            val gioHang = GioHang(
                maSP = sanPham.id_sanPham.toString(), // Chuyển id_sanPham thành String
                tenSP = sanPham.ten_sp,
                loaiSP = loaiSP, // Nếu idLoai_sp là đối tượng, cần lấy giá trị cụ thể, ví dụ: idLoai_sp.tenLoai
                gia = sanPham.gia_sp,
                soLuong = sanPham.soLuong_sp,
                hinh = sanPham.img_sp,
                daChon = 0, // Giả sử sản phẩm này đã được chọn (1 = true, 0 = false)
                maKH = DangNhap.maKH // Thay bằng mã khách hàng phù hợp
            )

            // Gọi hàm updateGioHang để cập nhật thông tin giỏ hàng
            val result =dbGioHang.updateGioHang(
                maSP = gioHang.maSP,
                maKH = gioHang.maKH,
                gioHang = gioHang
            )



        }



        gioHangAdapter = GioHangAdapter(
            this,
            R.layout.giohang_adapter,
            dsGioHang,
            this,
            maKH
        )
        lvDanhSachGioHang.adapter = gioHangAdapter

        //dbGioHang.deleteAllGioHang()

        dbGioHang.updateAllDaChonToZero(maKH)

        tingTongTien()
        //khoiTao() // Thêm dữ liệu vào danh sách
        loadGioHang()

        btnDatHang.setOnClickListener {
            if (dbGioHang.isAnyProductSelected(maKH)) {
                // Tạo Intent để chuyển từ MainActivity sang SecondActivity
                val intent = Intent(this, MainActivityDatHang::class.java)
                startActivity(intent) // Khởi chạy Intent
            }
            else{
                Toast.makeText(this, "Vui lòng chọn sản phẩm để đặt hàng !", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        imgBack.setOnClickListener {
            onBackPressed()
        }

        tvDonHang.setOnClickListener {
            val intent = Intent(this, MainActivityDonHang::class.java)
            startActivity(intent) // Khởi chạy Intent
        }

        btn_del_giohang.setOnClickListener {
            // Kiểm tra xem có sản phẩm nào đã được chọn không
            if (dbGioHang.isAnyProductSelected(maKH)) {
                // Nếu có ít nhất một sản phẩm đã chọn, yêu cầu xác nhận xóa
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Bạn có chắc chắn muốn xóa tất cả sản phẩm đã chọn?")
                    .setCancelable(false)
                    .setPositiveButton("Có") { dialog, id ->
                        // Nếu người dùng chọn "Có", thực hiện xóa sản phẩm đã chọn
                        dbGioHang.deleteGioHangDaChon(maKH)
                        loadGioHang() // Cập nhật lại giỏ hàng
                    }
                    .setNegativeButton("Không") { dialog, id ->
                        // Nếu người dùng chọn "Không", đóng dialog mà không làm gì cả
                        dialog.dismiss()
                    }

                // Hiển thị AlertDialog
                val alert = builder.create()
                alert.show()
            } else {
                // Nếu không có sản phẩm nào được chọn, hiển thị thông báo
                Toast.makeText(this, "Không có sản phẩm nào được chọn để xóa", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun khoiTao() {

        var stringBase64: StringBase64 = StringBase64()
        // Create GioHang objects and insert into the database
        val gioHang1 = GioHang(
            "001",
            "Dầu ăn Simply đậu nành 5L",
            "Dầu ăn",
            136000 * 1.0,
            1,
            stringBase64.base64_anh4,
            0,
            maKH
        )
        val gioHang2 = GioHang(
            "002",
            "Snack vị tảo biển Hàn Quôc nướng Lays gói 152g",
            "Snack",
            16000 * 1.0,
            1,
            stringBase64.base64_anh3,
            0,
            maKH
        )

        val gioHang3 = GioHang(
            "003",
            "Snack vị bí đỏ Hàn Quôc nướng Lays gói 500g",
            "Snack",
            16000 * 1.0,
            1,
            stringBase64.base64_anh5,
            0,
            maKH
        )


        // Insert into the database
        dbGioHang.insertGioHang(gioHang1)
        dbGioHang.insertGioHang(gioHang2)
        dbGioHang.insertGioHang(gioHang3)
        // Load updated data from the database
        loadGioHang()
    }

    private fun loadGioHang() {
        // Lấy danh sách giỏ hàng từ cơ sở dữ liệu
        dsGioHang.clear()
        dsGioHang.addAll(dbGioHang.getAllGioHang(maKH))
        gioHangAdapter.notifyDataSetChanged()
    }


    fun tingTongTien() {
        val totalPrice = dbGioHang.getTotalPriceOfSelectedProducts(maKH)

        // Kiểm tra và định dạng giá trị
        val formattedPrice = if (totalPrice % 1 == 0.0) {
            // Nếu không có số thập phân đáng kể, chỉ giữ phần nguyên
            String.format("%,.0f", totalPrice)
        } else {
            // Giữ nguyên định dạng 2 chữ số thập phân
            String.format("%,.2f", totalPrice)
        }

        // Cập nhật giao diện
        tvTongTien.text = "$formattedPrice đ"
        tvTienHang.text = "$formattedPrice đ"
        tvTongDonHang.text = "$formattedPrice đ"
    }

}

package com.example.dangnhapdangki.Activity


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.Database.DataBaseDonHang
import com.example.dangnhapdangki.Database.DataBaseGioHang
import com.example.dangnhapdangki.Database.DataBaseNapDiem
import com.example.dangnhapdangki.Model.DonHang
import com.example.dangnhapdangki.Model.GioHang
import com.example.dangnhapdangki.SmallActivity
import com.example.prj_qlbh.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivityDatHang : AppCompatActivity() {
    private lateinit var tvNapDiem: TextView
    private lateinit var ivBackDH: ImageView
    private lateinit var btnDatHang: Button
    private lateinit var tvPhuongThucThanhToan: TextView
    private lateinit var linearLayoutPhuongThuc: LinearLayout
    private lateinit var tvChuyenKhoan: TextView
    private lateinit var tvTienMat: TextView
    private lateinit var tvDiemHienCo: TextView
    private lateinit var tvDiemCanThanhToan: TextView
    private lateinit var edtTen: EditText
    private lateinit var edtSDT: EditText
    private lateinit var edtTinhTP: EditText
    private lateinit var edtQuanHuyen: EditText
    private lateinit var edtQuanXa: EditText
    private lateinit var edtSoDuong: EditText

    private val dbGioHang: DataBaseGioHang by lazy { DataBaseGioHang(this) }
    private val dbNapDiem: DataBaseNapDiem by lazy { DataBaseNapDiem(this) }
    private val dbDonHang: DataBaseDonHang by lazy { DataBaseDonHang(this) }

    private val dsGioHang = ArrayList<GioHang>()

    private var maKH: String = DangNhap.maKH //MaKhachHang

    private lateinit var handler: Handler
    private lateinit var updateRunnable: Runnable

    companion object {
        var idNgay = String()
        var maKH: String = "001" //MaKhachHang
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dathang)



        getControl()
        getEvent()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            // Nhận dữ liệu từ SmallActivity
//            val result = data?.getStringExtra("phuongThucThanhToan") ?: ""
//            tvPhuongThucThanhToan.text = result // Cập nhật giá trị
//        }
//    }

    fun getControl() {
        tvNapDiem = findViewById(R.id.tvNapDiem)
        ivBackDH = findViewById(R.id.ivBackDH)
        btnDatHang = findViewById(R.id.btnDatHang)
        tvPhuongThucThanhToan = findViewById(R.id.tvPhuongThucThanhToan)
        linearLayoutPhuongThuc = findViewById(R.id.linearLayoutPhuongThuc)
        tvTienMat = findViewById(R.id.tvTienMat)
        tvChuyenKhoan = findViewById(R.id.tvChuyenKhoan)
        tvDiemHienCo = findViewById(R.id.tvDiemHienCo)
        tvDiemCanThanhToan = findViewById(R.id.tvDiemCanThanhToan)
        edtTen = findViewById(R.id.edtTen)
        edtSDT = findViewById(R.id.edtSDT)
        edtTinhTP = findViewById(R.id.edtTinhTP)
        edtQuanHuyen = findViewById(R.id.edtQuanHuyen)
        edtQuanXa = findViewById(R.id.edtQuanXa)
        edtSoDuong = findViewById(R.id.edtSoDuong)

    }

    fun getEvent() {


        // Lấy tổng số điểm cần thanh toán từ cơ sở dữ liệu
        val totalPrice = dbGioHang.getTotalPriceOfSelectedProducts(maKH)

        // Kiểm tra và định dạng số điểm
        val formattedPrice = if (totalPrice % 1 == 0.0) {
            // Nếu là số nguyên, chỉ hiển thị phần nguyên
            String.format("%,.0f", totalPrice)
        } else {
            // Giữ nguyên định dạng với 2 chữ số thập phân
            String.format("%,.2f", totalPrice)
        }

        // Hiển thị số điểm cần thanh toán trên TextView
        tvDiemCanThanhToan.text = "Số điểm cần phải thanh toán: $formattedPrice điểm"


        handler = Handler(mainLooper)
        updateRunnable = object : Runnable {
            override fun run() {
                updateDiemHienTai()  // Cập nhật điểm hiện tại
                handler.postDelayed(this, 3000)  // Lặp lại sau 5 giây (5000ms)
            }
        }

        // Bắt đầu quá trình cập nhật điểm mỗi 5 giây
        handler.post(updateRunnable)


        linearLayoutPhuongThuc.visibility = View.GONE
        // Đặt sự kiện onClick cho tvNapDiem
        tvNapDiem.setOnClickListener {
            // Tạo Intent để chuyển từ MainActivity sang SecondActivity
            val intent = Intent(this, MainActivityNapDiem::class.java)
            startActivity(intent) // Khởi chạy Intent
        }

        ivBackDH.setOnClickListener {
            onBackPressed()
        }


        // Mở SmallActivity khi nhấn nút
        tvPhuongThucThanhToan.setOnClickListener {
            linearLayoutPhuongThuc.visibility = View.VISIBLE
            tvPhuongThucThanhToan.visibility = View.VISIBLE
        }

        tvChuyenKhoan.setOnClickListener {
            linearLayoutPhuongThuc.visibility = View.GONE
            tvPhuongThucThanhToan.visibility = View.VISIBLE
            tvPhuongThucThanhToan.text = "Chuyển Khoản"
        }

        tvTienMat.setOnClickListener {
            linearLayoutPhuongThuc.visibility = View.GONE
            tvPhuongThucThanhToan.visibility = View.VISIBLE
            tvPhuongThucThanhToan.text = "Tiền Mặt"
        }



        btnDatHang.setOnClickListener {
            if (validateAndGetInput() != null) {
                if (tvPhuongThucThanhToan.text == "Chuyển Khoản" || tvPhuongThucThanhToan.text == "Tiền Mặt") {


                    // Hiển thị hộp thoại xác nhận trước khi thanh toán
                    val dialog = AlertDialog.Builder(this)
                        .setTitle("Xác nhận thanh toán")
                        .setMessage("Bạn có chắc chắn muốn thanh toán với số điểm hiện có không?")
                        .setPositiveButton("Có") { _, _ ->
                            // Kiểm tra phương thức thanh toán
                            if (tvPhuongThucThanhToan.text == "Chuyển Khoản") {
                                // Nếu chọn phương thức Chuyển Khoản, kiểm tra và trừ điểm
                                if (dbNapDiem.canPayWithDiem(maKH, totalPrice.toInt())) {
                                    // Giảm điểm từ tài khoản khách hàng
                                    val newDiem = dbNapDiem.subtractDiem(maKH, totalPrice)

                                    val currentDate = getCurrentDateTime()
                                    dsGioHang.clear()
                                    dsGioHang.addAll(dbGioHang.getSelectedGioHang(maKH))

                                    idNgay = currentDate
                                    Toast.makeText(
                                        this,
                                        "Thanh toán thành công! Điểm còn lại: $newDiem",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Thêm các sản phẩm trong giỏ hàng vào bảng đơn hàng
                                    val diaChi =
                                        "" + edtTen.text + " - " + edtSDT.text + " - " + edtSoDuong.text + ", " + edtQuanXa.text + ", " + edtQuanHuyen.text + ", " + edtTinhTP.text
                                    val donHangList = mutableListOf<DonHang>()

                                    // Duyệt qua giỏ hàng để tạo danh sách đơn hàng
                                    for (gioHang in dsGioHang) {
                                        val donHang = DonHang(
                                            gioHang.maSP,
                                            gioHang.tenSP,
                                            gioHang.loaiSP,
                                            diaChi,
                                            currentDate,
                                            "Đã thanh toán (" + tvPhuongThucThanhToan.text + ")",
                                            gioHang.gia,
                                            gioHang.soLuong,
                                            gioHang.hinh,
                                            gioHang.maKH
                                        )
                                        maKH = gioHang.maKH
                                        donHangList.add(donHang)
                                    }

                                    // Chèn danh sách đơn hàng vào cơ sở dữ liệu
                                    val insertedIds = dbDonHang.insertDonHangList(donHangList)

                                    // Kiểm tra kết quả và chuyển sang SmallActivity nếu đơn hàng đã được tạo
                                    val intent = Intent(this, SmallActivity::class.java)
                                    startActivity(intent)  // Khởi chạy SmallActivity để kiểm tra trạng thái đơn hàng

                                } else {
                                    // Nếu không đủ điểm, thông báo lỗi
                                    Toast.makeText(
                                        this,
                                        "Không đủ điểm để thanh toán!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // Nếu chọn phương thức Tiền Mặt, không trừ điểm
                                val currentDate = getCurrentDateTime()
                                dsGioHang.clear()
                                dsGioHang.addAll(dbGioHang.getSelectedGioHang(maKH))

                                idNgay = currentDate
                                Toast.makeText(
                                    this,
                                    "Thanh toán thành công! Phương thức: Tiền Mặt",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Thêm các sản phẩm trong giỏ hàng vào bảng đơn hàng
                                val diaChi =
                                    "" + edtTen.text + " - " + edtSDT.text + " - " + edtSoDuong.text + ", " + edtQuanXa.text + ", " + edtQuanHuyen.text + ", " + edtTinhTP.text
                                val donHangList = mutableListOf<DonHang>()

                                // Duyệt qua giỏ hàng để tạo danh sách đơn hàng
                                for (gioHang in dsGioHang) {
                                    val donHang = DonHang(
                                        gioHang.maSP,
                                        gioHang.tenSP,
                                        gioHang.loaiSP,
                                        diaChi,
                                        currentDate,
                                        "Đã thanh toán (" + tvPhuongThucThanhToan.text + ")",
                                        gioHang.gia,
                                        gioHang.soLuong,
                                        gioHang.hinh,
                                        gioHang.maKH
                                    )
                                    maKH = gioHang.maKH
                                    donHangList.add(donHang)
                                }

                                // Chèn danh sách đơn hàng vào cơ sở dữ liệu
                                val insertedIds = dbDonHang.insertDonHangList(donHangList)

                                // Kiểm tra kết quả và chuyển sang SmallActivity nếu đơn hàng đã được tạo
                                finish() // Đóng SmallActivity
                                val intent = Intent(this, SmallActivity::class.java)
                                startActivity(intent)  // Khởi chạy SmallActivity để kiểm tra trạng thái đơn hàng
                            }
                        }
                        .setNegativeButton("Không") { dialogInterface, _ ->
                            // Nếu người dùng chọn "Không", đóng hộp thoại
                            dialogInterface.dismiss()
                        }

                    // Hiển thị hộp thoại xác nhận
                    dialog.show()
                } else {
                    Toast.makeText(
                        this,
                        "Chọn phương thức thanh toán !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {

            }

        }


    }

    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateTimeFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Định dạng ngày và giờ
        return dateTimeFormat.format(calendar.time) // Trả về ngày và giờ hiện tại
    }


    private fun updateDiemHienTai() {
        try {
            // Lấy số điểm hiện tại từ cơ sở dữ liệu
            val currentDiem = dbNapDiem.getDiem(maKH).toInt()
                ?: 0.0  // Nếu null, gán giá trị mặc định là 0.0

            // Định dạng số điểm theo kiểu "100.000"
            val format = DecimalFormat("#,###")
            val formattedDiem = format.format(currentDiem)

            // Hiển thị số điểm đã định dạng
            tvDiemHienCo.text = "Điểm hiện có: $formattedDiem điểm"
        } catch (e: Exception) {
            // Xử lý ngoại lệ (có thể log lỗi hoặc hiển thị thông báo cho người dùng)
            e.printStackTrace()
            tvDiemHienCo.text = "Không thể tải điểm hiện có."
        }


    }

    fun validateAndGetInput(): String? {
        when {
            edtTen.text.isNullOrEmpty() -> {
                edtTen.error = "Vui lòng nhập tên"
                edtTen.requestFocus()
                return null
            }

            edtSDT.text.isNullOrEmpty() -> {
                edtSDT.error = "Vui lòng nhập số điện thoại"
                edtSDT.requestFocus()
                return null
            }

            edtTinhTP.text.isNullOrEmpty() -> {
                edtTinhTP.error = "Vui lòng nhập tỉnh/thành phố"
                edtTinhTP.requestFocus()
                return null
            }

            edtQuanHuyen.text.isNullOrEmpty() -> {
                edtQuanHuyen.error = "Vui lòng nhập quận/huyện"
                edtQuanHuyen.requestFocus()
                return null
            }

            edtQuanXa.text.isNullOrEmpty() -> {
                edtQuanXa.error = "Vui lòng nhập xã/phường"
                edtQuanXa.requestFocus()
                return null
            }


            edtSoDuong.text.isNullOrEmpty() -> {
                edtSoDuong.error = "Vui lòng nhập số đường"
                edtSoDuong.requestFocus()
                return null
            }


        }

        // Nếu tất cả đều hợp lệ, ghép chuỗi và trả về kết quả
        return edtTen.text.toString() + " - " +
                edtSDT.text.toString() + " - " +
                edtSoDuong.text.toString() + ", " +
                edtQuanXa.text.toString() + ", " +
                edtQuanHuyen.text.toString() + ", " +
                edtTinhTP.text.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Dừng việc cập nhật khi Activity bị hủy
        handler.removeCallbacks(updateRunnable)
    }
}
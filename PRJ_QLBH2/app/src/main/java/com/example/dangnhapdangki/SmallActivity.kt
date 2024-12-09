package com.example.dangnhapdangki

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.Activity.MainActivityDatHang
import com.example.dangnhapdangki.Activity.MainActivityDonHang
import com.example.dangnhapdangki.Database.DataBaseDonHang
import com.example.prj_qlbh.R

class SmallActivity : AppCompatActivity() {

    private lateinit var tvOrderStatus: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnClose: Button

    private val dbDonHang: DataBaseDonHang by lazy { DataBaseDonHang(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_small)

        // Lấy kích thước màn hình hiện tại
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val screenWidth = metrics.widthPixels  // Chiều rộng màn hình
        val screenHeight = metrics.heightPixels  // Chiều cao màn hình

        // Tính toán chiều rộng và chiều cao cho cửa sổ của bạn
        val params: WindowManager.LayoutParams = window.attributes
        params.width = (screenWidth * 0.8).toInt()  // Chiều rộng cửa sổ là 80% màn hình
        params.height = (screenHeight * 0.6).toInt()  // Chiều cao cửa sổ là 60% màn hình

        // Áp dụng lại các tham số của cửa sổ
        window.attributes = params

        setControl()
        setEvent()

        // Giả lập việc chờ đợi trong khi đang kiểm tra đơn hàng
        simulateOrderCheck()
    }

    private fun setControl() {
        tvOrderStatus = findViewById(R.id.tvOrderStatus)
        progressBar = findViewById(R.id.progressBar)
        btnClose = findViewById(R.id.btnClose)
    }

    private fun setEvent() {
        btnClose.setOnClickListener {
            finish() // Đóng màn hình nhỏ
            val intent = Intent(this, MainActivityDonHang::class.java)
            startActivity(intent)
        }
    }

    private fun simulateOrderCheck() {
        // Giả lập việc kiểm tra đơn hàng (chờ đợi 3 giây)
        Handler().postDelayed({
            // Sau khi chờ đợi, kiểm tra đơn hàng thành công
            progressBar.visibility = ProgressBar.INVISIBLE // Ẩn ProgressBar

            if (dbDonHang.isOrderInsertedByDate(
                    MainActivityDatHang.maKH,
                    MainActivityDatHang.idNgay
                )
            ) {
                tvOrderStatus.text = "Đơn hàng đã được đặt thành công!"
            }
            else{
                tvOrderStatus.text = "Đơn hàng chưa được đặt thành công!"
            }
            btnClose.visibility = View.GONE

            // Delay thêm 1 giây trước khi chuyển trang
            Handler().postDelayed({
                // Đảm bảo đóng SmallActivity trước khi chuyển trang
                finish() // Đóng SmallActivity
                val intent = Intent(this, MainActivityDonHang::class.java)
                startActivity(intent)
            }, 1000) // Chờ 1 giây trước khi chuyển trang

        }, 1100) // Giả lập thời gian chờ đợi 2 giây
    }


}

package vn.edu.tdc.hongoccanh_doan_04th11

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.MainActivityDonHang
import com.example.dangnhapdangki.MainActivityNapDiem
import com.example.dangnhapdangki.SmallActivity
import com.example.prj_qlbh.R

class MainActivityDatHang : AppCompatActivity() {
    private lateinit var tvNapDiem: TextView
    private lateinit var ivBackDH: ImageView
    private lateinit var btnDatHang: Button
    private lateinit var tvPhuongThucThanhToan: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dathang)

        // Mở SmallActivity khi nhấn nút
        tvPhuongThucThanhToan.setOnClickListener {
            startActivity(Intent(this, SmallActivity::class.java))
        }

        getControl()
        getEvent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Nhận dữ liệu từ SmallActivity
            val result = data?.getStringExtra("phuongThucThanhToan") ?: ""
            tvPhuongThucThanhToan.text = result // Cập nhật giá trị
        }
    }

    fun getControl() {
        tvNapDiem = findViewById(R.id.tvNapDiem)
        ivBackDH = findViewById(R.id.ivBackDH)
        btnDatHang = findViewById(R.id.btnDatHang)
        tvPhuongThucThanhToan = findViewById(R.id.tvPhuongThucThanhToan)
    }

    fun getEvent() {
        // Đặt sự kiện onClick cho tvNapDiem
        tvNapDiem.setOnClickListener {
            // Tạo Intent để chuyển từ MainActivity sang SecondActivity
            val intent = Intent(this, MainActivityNapDiem::class.java)
            startActivity(intent) // Khởi chạy Intent
        }

        ivBackDH.setOnClickListener {
            onBackPressed()
        }

        btnDatHang.setOnClickListener {
            val intent = Intent(this, MainActivityDonHang::class.java)
            startActivity(intent) // Khởi chạy Intent
        }
    }
}
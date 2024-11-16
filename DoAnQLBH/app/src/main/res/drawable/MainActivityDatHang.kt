package vn.edu.tdc.hongoccanh_doan_04th11

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivityDatHang : AppCompatActivity() {
    private lateinit var tvNapDiem: TextView
    private lateinit var ivBackDH: ImageView
    private lateinit var btnDatHang: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dathang)

        // Mở SmallActivity khi nhấn nút
        findViewById<TextView>(R.id.open_small_activity_tv).setOnClickListener {
            startActivity(Intent(this, SmallActivity::class.java))
        }

        getControl()
        getEvent()
    }

    fun getControl() {
        tvNapDiem = findViewById(R.id.tvNapDiem)
        ivBackDH = findViewById(R.id.ivBackDH)
        btnDatHang = findViewById(R.id.btnDatHang)
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
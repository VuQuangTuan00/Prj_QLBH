package com.example.dangnhapdangki.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.Database.DataBaseNapDiem
import com.example.prj_qlbh.R

class MainActivityNapDiem : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvDiemHienTai: TextView
    private lateinit var edtNapDiem: EditText
    private lateinit var dbNapDiem: DataBaseNapDiem

    private  var maKH: String = "001" //MaKhachHang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_nap_diem)

        setControl()
        setEvent()

        dbNapDiem = DataBaseNapDiem(this)

        // Hiển thị số điểm hiện tại lên giao diện
        updateDiemHienTai()
    }

    private fun setEvent() {
        // Đặt sự kiện onClick cho ivBack
        ivBack.setOnClickListener {
            onBackPressed()
        }

        // Đặt sự kiện khi người dùng nạp thêm điểm
        findViewById<Button>(R.id.btnNapDiem).setOnClickListener {
            val napDiem = edtNapDiem.text.toString().toIntOrNull() ?: 0
            if (napDiem > 0) {
                dbNapDiem.addDiem(maKH, napDiem)  // Thêm điểm vào cơ sở dữ liệu
                updateDiemHienTai()  // Cập nhật lại giao diện
                Toast.makeText(this, "Đã nạp $napDiem điểm!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Điểm nhập vào không hợp lệ", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun setControl() {
        ivBack = findViewById(R.id.ivBack)
        tvDiemHienTai = findViewById(R.id.tvDiemHienTai)  // TextView để hiển thị điểm hiện tại
        edtNapDiem = findViewById(R.id.edtNapDiem)  // EditText để nhập điểm nạp thêm
    }

    private fun updateDiemHienTai() {
        val currentDiem = dbNapDiem.getDiem(maKH)  // Lấy số điểm hiện tại từ cơ sở dữ liệu
        tvDiemHienTai.text = "$currentDiem điểm"  // Cập nhật TextView với điểm hiện tại
    }
}

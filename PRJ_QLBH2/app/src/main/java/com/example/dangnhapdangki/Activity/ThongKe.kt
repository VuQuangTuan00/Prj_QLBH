package com.example.dangnhapdangki.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.ActivityThongKeBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ThongKe : AppCompatActivity() {
    private lateinit var binding:ActivityThongKeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_thong_ke)
        val pieChart = findViewById<PieChart>(R.id.barchart)

        // Lấy dữ liệu từ SQLite
        val dbHelper = SanPhamDBHelper(this)
        val statistics = dbHelper.getProductStatistics()

        // Chuyển đổi dữ liệu thành PieEntry
        val entries = statistics.map { (key, value) -> PieEntry(value, key) }

        // Tạo PieDataSet
        val dataSet = PieDataSet(entries, "Thống kê sản phẩm")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 12f

        // Gán dữ liệu vào PieChart
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.text = "Thống kê số lượng sản phẩm theo loại"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }
}
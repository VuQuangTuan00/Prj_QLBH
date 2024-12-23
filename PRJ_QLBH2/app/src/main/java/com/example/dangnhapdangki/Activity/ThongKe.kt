package com.example.dangnhapdangki.Activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dangnhapdangki.Database.SanPhamDBHelper
import com.example.prj_qlbh.databinding.ActivityThongKeBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class ThongKe : AppCompatActivity() {
    private lateinit var binding: ActivityThongKeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThongKeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy PieChart và BarChart từ XML
        val pieChart: PieChart = binding.pieChart
        val barChart: BarChart = binding.barChart
        val pieChartTitle: TextView = binding.pieChartTitle
        val barChartTitle: TextView = binding.barChartTitle

        // Lấy dữ liệu từ SQLite
        val dbHelper = SanPhamDBHelper(this)
        val statistics = dbHelper.getCategoryProductStatistics()

        // Tạo dữ liệu cho PieChart
        val entries = statistics.map { (key, value) -> PieEntry(value, key) }
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 14f

        // Gán dữ liệu vào PieChart
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false // Tắt mô tả mặc định
        pieChart.setExtraOffsets(5f, 10f, 5f, 10f)
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(android.graphics.Color.WHITE)
        pieChart.animateY(1000)
        pieChart.invalidate()

        // Tạo dữ liệu cho BarChart
        val statistic = dbHelper.getProductStatistics()
        val barEntries = statistic.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }
        val barDataSet = BarDataSet(barEntries, "")
        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        barDataSet.valueTextSize = 14f

        // Gán dữ liệu vào BarChart
        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()

        // Bạn có thể thay đổi tiêu đề nếu cần
        pieChartTitle.text = "Thống kê loại sản phẩm"
        barChartTitle.text = "Thống kê sản phẩm"
    }
}

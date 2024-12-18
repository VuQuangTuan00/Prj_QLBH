package com.example.demo_recycleview.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.prj_qlbh.R
import com.example.prj_qlbh.databinding.LayoutItemLoaispBinding

class AdapterLoaiSanPham(
    private val dsLoaiSP: ArrayList<LoaiSanPham>,
    private val listener: OnLoaiSanPhamClickListener
) : RecyclerView.Adapter<AdapterLoaiSanPham.LoaiSanPhamViewHolder>() {


    var selectedPosition: Int = -1
    lateinit var tvLoaiSP: TextView




    inner class LoaiSanPhamViewHolder(val binding: LayoutItemLoaispBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loaiSanPham: LoaiSanPham, position: Int) {

            binding.tvLoaiSP.text = loaiSanPham.tenLoai_sp


            // Đổi màu nền dựa trên trạng thái được chọn
            if (selectedPosition == position) {
                tvLoaiSP = binding.tvLoaiSP
                binding.tvLoaiSP.setBackgroundResource(R.drawable.brown_bg)
            } else {
                binding.tvLoaiSP.setBackgroundResource(R.drawable.editext_bg)
            }

            // Gắn sự kiện click
            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition) // Làm mới mục trước đó
                notifyItemChanged(selectedPosition) // Làm mới mục hiện tại

                listener.onLoaiSanPhamClick(loaiSanPham) // Gọi callback
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoaiSanPhamViewHolder {
        val binding =
            LayoutItemLoaispBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoaiSanPhamViewHolder(binding)
    }

    override fun getItemCount(): Int = dsLoaiSP.size

    override fun onBindViewHolder(holder: LoaiSanPhamViewHolder, position: Int) {
        holder.bind(dsLoaiSP[position], position)
    }
}

interface OnLoaiSanPhamClickListener {
    fun onLoaiSanPhamClick(loaiSanPham: LoaiSanPham)
}

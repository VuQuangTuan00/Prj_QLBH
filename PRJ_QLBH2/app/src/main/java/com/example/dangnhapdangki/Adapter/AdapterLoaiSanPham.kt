package com.example.demo_recycleview.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_recycleview.Model.LoaiSanPham
import com.example.demo_recycleview.R
import com.example.demo_recycleview.databinding.LayoutItemLoaispBinding

class AdapterLoaiSanPham(
    var dsLoaiSP: ArrayList<LoaiSanPham>
) : RecyclerView.Adapter<AdapterLoaiSanPham.LoaiSanPhamViewHolder>() {
    private lateinit var binding: LayoutItemLoaispBinding
    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    inner class LoaiSanPhamViewHolder(val binding: LayoutItemLoaispBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoaiSanPhamViewHolder {
        binding =
            LayoutItemLoaispBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoaiSanPhamViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (dsLoaiSP.isNotEmpty()) {
            dsLoaiSP.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: LoaiSanPhamViewHolder, position: Int) {
        holder.binding.tvLoaiSP.text = dsLoaiSP[position].tenLoai_sp
        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
        if (selectedPosition == position){
            holder.binding.tvLoaiSP.setBackgroundResource(R.drawable.brown_bg)
        }else{
            holder.binding.tvLoaiSP.setBackgroundResource(R.drawable.editext_bg)
        }
    }
}
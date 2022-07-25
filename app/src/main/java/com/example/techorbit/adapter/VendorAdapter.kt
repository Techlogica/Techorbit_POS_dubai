package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.SingleTerminalSalesReportBinding
import com.example.techorbit.databinding.VendorLayoutSingleBinding
import com.example.techorbit.model.reports.vendor.Data
import kotlinx.android.synthetic.main.vendor_layout_single.view.*

class VendorAdapter : RecyclerView.Adapter<VendorAdapter.ViewHolder>() {

    var list = ArrayList<Data>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VendorAdapter.ViewHolder {
        return VendorAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.vendor_layout_single, parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VendorAdapter.ViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }
        holder.bind(list[position])
    }

    class ViewHolder(val view: VendorLayoutSingleBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(data: Data) {
            view.data = data

        }
    }

    fun bindData(data: List<Data>) {
        notifyDataSetChanged()
        list = data as ArrayList<Data>
    }
}

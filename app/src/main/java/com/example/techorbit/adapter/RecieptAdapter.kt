package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.LayoutSingleReceiptBinding
import com.example.techorbit.model.reciept.Data
import kotlinx.android.synthetic.main.vendor_layout_single.view.*

class RecieptAdapter : RecyclerView.Adapter<RecieptAdapter.ViewHolder>() {
    var list = ArrayList<Data>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecieptAdapter.ViewHolder {
        return RecieptAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_single_receipt, parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecieptAdapter.ViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }
        holder.bind(list[position])
    }

    class ViewHolder(val view: LayoutSingleReceiptBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(data: Data) {
            view.data = data

        }
    }

    fun bindData(data: List<Data>) {

        list = data as ArrayList<Data>
        notifyDataSetChanged()
    }
}

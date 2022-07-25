package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.CommissionSingleBinding
import com.example.techorbit.model.me.CommissionRate
import kotlinx.android.synthetic.main.commission_single.view.*

class CommissionAdapter : RecyclerView.Adapter<CommissionAdapter.ViewHolder>() {

    var list = ArrayList<CommissionRate>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommissionAdapter.ViewHolder {
        return CommissionAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.commission_single,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommissionAdapter.ViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.itemView.constraint_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.constraint_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }
        holder.bindata(list[position])
    }

    fun bind(data: List<CommissionRate>) {

        list= data as ArrayList<CommissionRate>
        notifyDataSetChanged()
    }

    class ViewHolder(val itemview: CommissionSingleBinding) :
        RecyclerView.ViewHolder(itemview.root) {
        fun bindata(commissionRate: CommissionRate) {
            itemview.data = commissionRate
        }

    }
}
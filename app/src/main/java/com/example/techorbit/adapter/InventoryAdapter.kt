package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.InventorySingleBinding
import com.example.techorbit.model.reports.inventory.Inventory
import kotlinx.android.synthetic.main.vendor_layout_single.view.*

class InventoryAdapter : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    var list = ArrayList<Inventory>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryAdapter.ViewHolder {
        return InventoryAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.inventory_single, parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: InventoryAdapter.ViewHolder, position: Int) {
        if (position % 2 == 1) {

            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))
        } else {

            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        holder.bind(list[position])
    }

    class ViewHolder(val view: InventorySingleBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(inventory: Inventory) {
            view.data=inventory
        }

    }

    fun bindData(data: List<Inventory>?) {
        notifyDataSetChanged()
        list= data as ArrayList<Inventory>

    }
}

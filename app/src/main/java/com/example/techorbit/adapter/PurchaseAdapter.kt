package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.LayoutPurchaseSingleBinding
import com.example.techorbit.databinding.VendorLayoutSingleBinding
import com.example.techorbit.model.purchase.Data
import kotlinx.android.synthetic.main.vendor_layout_single.view.*

class PurchaseAdapter : RecyclerView.Adapter<PurchaseAdapter.ViewHolder>() {

    var list = ArrayList<Data>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseAdapter.ViewHolder {
        return PurchaseAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_purchase_single, parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PurchaseAdapter.ViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }
        holder.bind(list[position])
    }

    class ViewHolder(val view: LayoutPurchaseSingleBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(data: Data) {
            view.data = data
            if (data.transactionMode=="CREDIT"){
                view.debit.text = "AED ${data.amount}"

            }
            if (data.transactionMode=="DEBIT"){
                view.credit.text = "AED ${data.amount}"
            }

        }
    }

    fun bindData(data: List<Data>) {
        notifyDataSetChanged()
        list = data as ArrayList<Data>
    }
}

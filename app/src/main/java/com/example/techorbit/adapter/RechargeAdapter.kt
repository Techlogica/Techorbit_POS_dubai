package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.model.prepaidplans.Data
import kotlinx.android.synthetic.main.evoucher_single_layout.view.*

class RechargeAdapter(val click: (Data) -> Unit) :
    RecyclerView.Adapter<RechargeAdapter.ViewHolder>() {


    private var list = ArrayList<Data>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RechargeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.evoucher_single_layout, parent, false)
        return RechargeAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RechargeAdapter.ViewHolder, position: Int) {
//        if (position % 2 == 1) {
//            holder.itemView.materialCardView.setBackgroundColor(Color.parseColor("#FFFFFF"))
//        } else {
//            holder.itemView.materialCardView.setBackgroundColor(Color.parseColor("#DDDDDD"))
//        }

        holder.itemView.benefits.text = list[position].benefits
        holder.itemView.data.text = list[position].mrp
        holder.itemView.validity.text = "validity:"+list[position].validity
        holder.itemView.abrivation.text=list[position].localCurrency
        holder.itemView.mrp.text=list[position].rechargeAmount

        holder.bindData(click, list[position])
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindData(clickdata: (Data) -> Unit, data: Data) {
            itemView.setOnClickListener {
                clickdata(data)
            }

        }

    }

    fun bindData(datalist: ArrayList<Data>) {
        list = datalist
        notifyDataSetChanged()
    }
}
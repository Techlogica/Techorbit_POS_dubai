package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import kotlinx.android.synthetic.main.evoucher_single_layout.view.*

class EvocherAdapter(val click: (String) -> Unit) : RecyclerView.Adapter<EvocherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvocherAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.evoucher_single_layout, parent, false)
        return EvocherAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: EvocherAdapter.ViewHolder, position: Int) {
        if (position%2 == 1){
            holder.itemView.materialCardView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }else{
            holder.itemView.materialCardView.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }

        holder.bind(click)
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(click: (String) -> Unit) {
            itemView.setOnClickListener {
                val data=itemView.data.text.toString()
                click(data)
            }
        }

    }
}
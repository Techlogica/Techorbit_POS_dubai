package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.model.five.Recharge
import kotlinx.android.synthetic.main.live_single_layout.view.*
import kotlin.random.Random

class LiveAdapter(val click: (Recharge) -> Unit) : RecyclerView.Adapter<LiveAdapter.ViewHolder>() {

    var list = ArrayList<Recharge>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.live_single_layout, parent, false)
        return LiveAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LiveAdapter.ViewHolder, position: Int) {
//        if (position % 2 == 1) {
//            holder.itemView.materialCardView.setBackgroundColor(Color.parseColor("#FFFFFF"))
//        } else {
//            holder.itemView.materialCardView.setBackgroundColor(Color.parseColor("#DDDDDD"))
//
//        }

//        val mColors = arrayOf(
//            "#DDA0DD","#EE82EE","#C71585","#FAEBD7","#FFB6C1","#E6E6FA"
//        )
//        val index= Random.nextInt(mColors.size-1)
//        holder.itemView.materialCardView.setBackgroundColor(Color.parseColor(mColors[index]))
        holder.itemView.benefits.text=list[position].benefits
        holder.itemView.data.text=list[position].mrp
        holder.bind(click, list[position])


    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(
            click: (Recharge) -> Unit,
            recharge: Recharge
        ) {
            itemView.setOnClickListener {
                val data = itemView.data.text.toString()
                click(recharge)
            }
        }

    }

    fun BindData(recharge: List<Recharge>) {
        notifyDataSetChanged()
        list = recharge as ArrayList<Recharge>
    }
}
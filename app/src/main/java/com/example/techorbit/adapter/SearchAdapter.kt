package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.databinding.LayoutSearchBinding
import com.example.techorbit.model.search.Data
import kotlinx.android.synthetic.main.evoucher_single_layout.view.*
import kotlinx.android.synthetic.main.layout_search.view.*
import kotlinx.android.synthetic.main.live_single_layout.view.*
import kotlinx.android.synthetic.main.live_single_layout.view.materialCardView

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var list = ArrayList<Data>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return SearchAdapter.ViewHolder(
            LayoutSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {

        if (position%2 == 1){
            holder.itemView.constraint_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }else{
            holder.itemView.constraint_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }
        holder.bind(list[position])
    }

    fun bind(data: List<Data>) {

        notifyDataSetChanged()
        list= data as ArrayList<Data>
    }

    class ViewHolder(private val view: LayoutSearchBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(data: Data) {
            view.data = data
        }

    }


}
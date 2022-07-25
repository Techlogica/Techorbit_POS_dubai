package com.example.techorbit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.model.country.Data
import kotlinx.android.synthetic.main.country_single.view.*

class CountryAdapter(val click: (Data) -> Unit) :
    RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    private lateinit var list: List<Data>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_single, parent, false)
        return CountryAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CountryAdapter.ViewHolder, position: Int) {
//        holder.itemView.country.text = list[position].name


        holder.bind(click, list[position])

    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(selcted: (Data) -> Unit, data: Data) {
            itemView.country.text = data.name
            val resource = itemView.context.resources
            data.flagName?.let {
                val id = resource.getIdentifier(data.flagName, "drawable", itemView.context.packageName)
                itemView.flag.setImageResource(id)
            }

            itemView.setOnClickListener {
                selcted(data)
            }
        }

    }

    fun bindData(list: List<Data>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun filterList(filterdNames: ArrayList<Data>) {
        list = filterdNames
        notifyDataSetChanged()
    }
}

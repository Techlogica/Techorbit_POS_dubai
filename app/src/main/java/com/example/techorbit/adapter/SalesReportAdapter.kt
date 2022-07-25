package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.LayoutWalletReportSingleBinding
import com.example.techorbit.databinding.SingleTerminalSalesReportBinding
import com.example.techorbit.model.reports.terminal.Data
import kotlinx.android.synthetic.main.layout_search.view.*
import kotlinx.android.synthetic.main.single_terminal_sales_report.view.*

class SalesReportAdapter : RecyclerView.Adapter<SalesReportAdapter.ViewHolder>() {

    var list = ArrayList<Data>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SalesReportAdapter.ViewHolder {
        return SalesReportAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.single_terminal_sales_report, parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SalesReportAdapter.ViewHolder, position: Int) {
        if (position%2 == 1){
            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }else{
            holder.itemView.linear_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }
        holder.bind(list[position])
    }

    class ViewHolder(val view: SingleTerminalSalesReportBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(data: Data) {
            view.data=data

        }
    }

    fun bindData(data: List<Data>) {
        notifyDataSetChanged()
        list = data as ArrayList<Data>
    }
}
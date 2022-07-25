package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.LayoutWalletReportSingleBinding
import com.example.techorbit.model.reports.walletreports.Data

class WalletReportAdapter : RecyclerView.Adapter<WalletReportAdapter.ViewHolder>() {

    var list = ArrayList<Data>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WalletReportAdapter.ViewHolder {
        return WalletReportAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_wallet_report_single, parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WalletReportAdapter.ViewHolder, position: Int) {
        holder.Bind(list[position])
    }

    class ViewHolder(val view: LayoutWalletReportSingleBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun Bind(data: Data) {
            view.data = data
            if (data.creditDebit=="CREDIT") {

                view.relativeLayout.background=view.root.context.getDrawable(R.drawable.border_green)
            }else{
                view.relativeLayout.background=view.root.context.getDrawable(R.drawable.border_wallet_red)
            }

        }

    }

    fun bindData(data: List<Data>) {
        notifyDataSetChanged()
        list = data as ArrayList<Data>
    }
}
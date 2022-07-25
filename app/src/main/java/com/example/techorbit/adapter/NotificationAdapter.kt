package com.example.techorbit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.LayoutWalletReportSingleBinding
import com.example.techorbit.databinding.NotificationSingleBinding
import com.example.techorbit.model.notification.Data
import kotlinx.android.synthetic.main.notification_single.view.*
import kotlinx.android.synthetic.main.vendor_layout_single.view.*

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    var list = ArrayList<Data>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        return NotificationAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.notification_single, parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.itemView.constraint_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.constraint_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }
        holder.Bind(list[position])
    }

    class ViewHolder(val view: NotificationSingleBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun Bind(data: Data) {
            view.data=data
        }

    }

    fun bindData(data: List<Data>) {
        notifyDataSetChanged()
        list = data as ArrayList<Data>
    }
}

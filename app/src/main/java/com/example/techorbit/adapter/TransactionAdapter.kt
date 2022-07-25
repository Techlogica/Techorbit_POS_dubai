package com.example.techorbit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.format.DateFormat.format
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.techorbit.R
import com.example.techorbit.databinding.LayoutTransactionSingleBinding
import com.example.techorbit.model.Data
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.viewmodel.OtarDuViewmodel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_transaction_single.view.*
import java.lang.String.format
import java.nio.charset.Charset
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAdapter( val click:(Data)->Unit) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    var list = ArrayList<Data>()
    private lateinit var viewmodel: OtarDuViewmodel


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAdapter.ViewHolder {
        return TransactionAdapter.ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_transaction_single, parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.itemView.constraint_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.constraint_layout.setBackgroundColor(Color.parseColor("#DDDDDD"))

        }
        holder.bind(list[position],click)
    }

    class ViewHolder(val view: LayoutTransactionSingleBinding) :
        RecyclerView.ViewHolder(view.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(
            data: Data,
            click: (Data) -> Unit,
        ) {
            view.data = data
            if (data.rechargeStatus.toString().toUpperCase() == "PROCESSING" ||
                data.rechargeStatus.toString().toUpperCase() =="CREATED" ||
                data.rechargeStatus.toString().toUpperCase() =="RECEIVED" ||
                data.rechargeStatus.toString().toUpperCase() =="INITIATED") {
                view.print.text = "Check Status"
                view.checkStatus.visibility = View.GONE
                view.print.visibility = View.VISIBLE
            }else {
                view.print.text = "Print"
                view.checkStatus.visibility = View.GONE
                view.print.visibility = View.VISIBLE
            }
//            var Date = Date
            view.createdTime.text = dateConversion(data.createdTime!!)
            view.print.setOnClickListener {
                click(data)
            }
            view.checkStatus.setOnClickListener {
                click(data)
            }

            if (data.rechargeStatus == "SUCCESS"){
                view.card.setCardBackgroundColor(Color.parseColor("#3FF60B"))
            }else if (data.rechargeStatus == "FAILED"
                || data.rechargeStatus == "CANCELLED"
                || data.rechargeStatus == "FAILED_BY_ERROR"
                || data.rechargeStatus == "CANCELLING"){
                view.card.setCardBackgroundColor(Color.parseColor("#cb171e"))
            }else {
                view.card.setCardBackgroundColor(Color.parseColor("#F17D0F"))
            }

        }

        fun dateConversion(input: String): String? {
            var output = ""
            if (!input.isEmpty()) {
                try {
                    var date = input.replace("T"," ")
                    var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                    val newDate = format.parse(date)
                    format = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
                    output = format.format(newDate)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            return output
        }
    }


    fun bindData(data: List<Data>) {
        notifyDataSetChanged()
        list = data as ArrayList<Data>
    }

}

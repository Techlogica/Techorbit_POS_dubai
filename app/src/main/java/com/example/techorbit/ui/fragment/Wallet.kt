package com.example.techorbit.ui.fragment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.techorbit.R
import com.example.techorbit.adapter.WalletReportAdapter
import com.example.techorbit.model.reports.walletreports.Data
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.WalletReport
import kotlinx.android.synthetic.main.fragment_wallet.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class Wallet : Fragment() {


    private val viewmodel: WalletReport by viewModel()
    var start: String? = null
    var end: String? = null


    private lateinit var mCalender: Calendar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCalender = Calendar.getInstance()

        val monthString =
            DateFormat.format("MMM", Calendar.getInstance().timeInMillis) as String // Jun

        val day =
            DateFormat.format("dd", Calendar.getInstance().timeInMillis) as String // 20

        val year =
            DateFormat.format("yyyy", Calendar.getInstance().timeInMillis) as String // 2013

        setupData("$day $monthString $year")

        val datePickerDialog =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mCalender.set(Calendar.YEAR, year)
                mCalender.set(Calendar.MONTH, monthOfYear)
                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calender_start_text.setText(updateLabel())
                start = "${updateLabel()} 00:00:00"
            }

        calender_start.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerDialog, mCalender
                    .get(Calendar.YEAR), mCalender.get(Calendar.MONTH),
                mCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        val datePickerDialog_second =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mCalender.set(Calendar.YEAR, year)
                mCalender.set(Calendar.MONTH, monthOfYear)
                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calender_end_text.setText(updateLabel())
                end = "${updateLabel()} 23:59:59"
            }

        calender_end.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerDialog_second, mCalender
                    .get(Calendar.YEAR), mCalender.get(Calendar.MONTH),
                mCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

//        search_report.setOnClickListener {
//            progress_circular.visibility=View.VISIBLE
//            showToast("Loadingg")
//            showData(start,end)
//        }


    }

    private fun showData(start: String?, end: String?) {

        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viewmodel.getReports(head, start!!, end!!)
        viewmodel.liveData.observe(requireActivity(), Observer {
            progress_circular.visibility = View.GONE
            when (it) {
                is ResultOf.Succes -> {

                    val adapter = WalletReportAdapter()
                    recycler_view.adapter = adapter

                    if (it.result.data.isNotEmpty()) {
                        adapter.bindData(it.result.data)
                    } else {

//                        showToast("No data Available")
                    }
                }
                is ResultOf.Failiure -> {

                    showToast("something went wrong")
                }
            }
        })

    }

    private fun updateLabel(): String {
        val format = "MM/dd/yy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.US)

//        val date=simpleDateFormat.format(mCalender.time)
//        val dayOfWeek=DateFormat.format("EEEE", Calendar.getInstance().timeInMillis)
        val day =
            DateFormat.format("dd", mCalender.time) as String // 20

        val monthString =
            DateFormat.format("MMM", mCalender.time) as String // Jun

//        val monthNumber =
//            DateFormat.format("MM", mCalender.time) as String // 06

        val year =
            DateFormat.format("yyyy", mCalender.time) as String // 2013




        return "$day $monthString $year"


    }

    private fun setupData(s: String) {
        progress_circular.visibility = View.VISIBLE
        calender_start_text.text = "$s"
        calender_end_text.text = "$s"
        val hour = Calendar.getInstance().get(Calendar.HOUR)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        val second = Calendar.getInstance().get(Calendar.SECOND)

        start = "$s 00:00:00"
        end = "$s $hour:$minute:$second"

        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viewmodel.getReports(head, start!!, end!!)
        viewmodel.liveData.observe(requireActivity(), Observer { it ->

            progress_circular.visibility = View.GONE
            when (it) {
                is ResultOf.Succes -> {
                    val adapter = WalletReportAdapter()
                    recycler_view.adapter = adapter

                    if (it.result.data.isNotEmpty()) {
                        val data = it.result.data
                        data.sortedByDescending {
                            it.transactionId.toInt()

                        }
                        adapter.bindData(data)
                    } else {
                        showToast("No data")
                    }
                }
                is ResultOf.Failiure -> {
                    showToast("something went wrong")
                }
            }
        })
    }


}
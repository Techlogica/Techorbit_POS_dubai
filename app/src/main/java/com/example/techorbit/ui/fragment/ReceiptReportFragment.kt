package com.example.techorbit.ui.fragment

import android.app.DatePickerDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.techorbit.R
import com.example.techorbit.adapter.RecieptAdapter
import com.example.techorbit.model.reciept.Data
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.RecieptViewmodel
import kotlinx.android.synthetic.main.fragment_receipt_report.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReceiptReportFragment : Fragment() {
    var start: String? = null
    var end: String? = null
    private lateinit var mCalender: Calendar
    private val BLUETOOTH_ENABLE_REQUEST = 200
    private val viemodel: RecieptViewmodel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt_report, container, false)
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

        observeData()

        val datePickerDialog =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mCalender.set(Calendar.YEAR, year)
                mCalender.set(Calendar.MONTH, monthOfYear)
                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calender_start_text.setText(updateLabel())
                start = "${updateLabel()} 00:00:00 GMT"
            }

        calender_start.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerDialog, mCalender
                    .get(Calendar.YEAR), mCalender.get(Calendar.MONTH),
                mCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        val datePickerDialog_second =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mCalender.set(Calendar.YEAR, year)
                mCalender.set(Calendar.MONTH, monthOfYear)
                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calender_end_text.setText(updateLabel())
                end = "${updateLabel()} 23:59:59 GMT"
            }

        calender_end.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerDialog_second, mCalender
                    .get(Calendar.YEAR), mCalender.get(Calendar.MONTH),
                mCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        search_report.setOnClickListener {

            showData(start, end)
        }
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

        start = "$s 00:00:00 GMT"
        end = "$s 23:59:59 GMT"

        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)

        viemodel.getVendorReports(head, start!!, end!!)
    }


    private fun showData(start: String?, end: String?) {
        progress_circular.visibility = View.VISIBLE
        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viemodel.getVendorReports(head, start!!, end!!)


    }

    private fun observeData() {
        viemodel.liveData.observe(requireActivity(), androidx.lifecycle.Observer {
            progress_circular.visibility = View.GONE
            when (it) {
                is ResultOf.Succes -> {
                    nestedscrollview.visibility = View.VISIBLE
                    total.text = it.result.receiptAmountTotal
                    if (it.result.data.isNotEmpty()) {
                        val adapter = RecieptAdapter()
                        recycler_view.visibility = View.VISIBLE
                        recycler_view.adapter = adapter

                        adapter.bindData(it.result.data)
                        print_button.visibility = View.VISIBLE
                        totallinear.visibility = View.VISIBLE

                        print_button.setOnClickListener { view ->
                            if (BluetoothUtil().is_bluetooth_enable) {
                                PrinterFunction(requireContext())
                                    .call_report_receipt_printer_bluetooth(
                                        it.result.data as ArrayList<Data>,
                                        start,
                                        end
                                    )

                            } else enable_bluetooth()
                        }


                    } else {
                        print_button.visibility = View.GONE
                        recycler_view.visibility = View.GONE
                        totallinear.visibility = View.GONE
                        showToast("No data")
                    }

                }
                is ResultOf.Failiure -> {
                    showToast("Failed!Try again")
                }
            }

        })
    }

    private fun enable_bluetooth() {
        val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(bluetoothIntent, BLUETOOTH_ENABLE_REQUEST)
    }


}
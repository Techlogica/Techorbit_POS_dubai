package com.example.techorbit

import android.app.DatePickerDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.example.techorbit.adapter.SalesReportAdapter
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.TerminalSalesReports
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.*
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.calender_end
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.calender_end_text
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.calender_start
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.calender_start_text
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.print_button
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.progress_circular
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.recycler_view
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.search_report
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.totalcommission
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.totalcount
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.totalface_value
import kotlinx.android.synthetic.main.fragment_recharge_terminal_report.totallinear
import kotlinx.android.synthetic.main.fragment_terminals_report.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import  androidx.lifecycle.Observer
import com.example.techorbit.adapter.TransactionAdapter
import com.example.techorbit.model.Terminal
import com.example.techorbit.model.reports.terminal.Data
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_transaction.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import kotlin.collections.ArrayList
import kotlin.math.log
import kotlin.math.roundToInt

class RechargeTerminalReportFragment : Fragment() {
    private val viewmodel: TerminalSalesReports by viewModel()
    var start: String? = null
    var end: String? = null
    private lateinit var mCalender: Calendar
    private val BLUETOOTH_ENABLE_REQUEST = 200
    var totalAmount = ""
    var commisions = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_recharge_terminal_report, container, false)
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


        calender_start_text.text = "$s"
        calender_end_text.text = "$s"
        val hour = Calendar.getInstance().get(Calendar.HOUR)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        val second = Calendar.getInstance().get(Calendar.SECOND)

        start = "$s 00:00:00 GMT"
        end = "$s 23:59:59 GMT"

        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)

        progress_circular.visibility = View.VISIBLE

        viewmodel.getTransactionReports(head, start!!, end!!)
    }


    private fun showData(start: String?, end: String?) {
        progress_circular.visibility = View.VISIBLE
        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
//        viewmodel.getSalesReports(head, start!!, end!!)
        viewmodel.getTransactionReports(head, start!!, end!!)


    }

    private fun observeData() {
        viewmodel.rechargeliveData.observe(requireActivity(), Observer {
            progress_circular.visibility = View.GONE
            when (it) {
                is ResultOf.Succes -> {
                    setData(it.result)
                }
                is ResultOf.Failiure -> {
                    if (it.isNetworkError) {
                        showToast(it.message)
                    } else {
                        showToast("Failed")
                    }
                }

            }

        })
    }


    private fun enable_bluetooth() {
        val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(bluetoothIntent, BLUETOOTH_ENABLE_REQUEST)
    }

    private fun setData(it: Any) {
        var list = ArrayList<com.example.techorbit.model.Data>()
        var terminallist = ArrayList<Data>()
        val v = Gson().toJson(it)
        val response = JSONObject(v)
        val totalJSON: JSONObject = response.getJSONObject("total")
        val rechargeCount: String = totalJSON.getString("rechargeCount").toString()
//        recharge_count.setText("$rechargeCount Recharges")
        totalAmount = java.lang.Float.valueOf(totalJSON.getString("rechargeAmount")).toString()
//        facevalue.text = "AED $totalAmount"
        commisions = java.lang.Float.valueOf(totalJSON.getString("commissionAmount")).toString()
//        commission.text = "AED $commisions"
        val currency = response.getString("currencyAbbreviation")


        val transactionListJSON: JSONArray = response.getJSONArray("data")

        if (transactionListJSON.length() <= 0) {
            showToast("No Data")
            return
        }

        for (i in 0 until transactionListJSON.length()) {
            val singleTransaction = com.example.techorbit.model.Data()
            val singleTransactionJSON = transactionListJSON.getJSONObject(i)

            if (singleTransactionJSON.getString("type") == "DOWNLOADCARD") {
//                singleTransaction.se("Pin Download")
//                singleTransaction.setBeneficiaryNumber(singleTransactionJSON.getString("quantity"))
            } else {
                var beneficiaryNumber =
                    singleTransactionJSON.getString("beneficiaryNumber")
                if (beneficiaryNumber == "null") beneficiaryNumber = "-"
                singleTransaction.beneficiaryNumber = beneficiaryNumber
            }
            val id = singleTransactionJSON.getString("rechargeId").toFloat()
            val df = DecimalFormat("###.#")
            val rechargeid = df.format(id)
            singleTransaction.rechargeId = rechargeid.toInt()
            singleTransaction.beneficiaryCountry =
                singleTransactionJSON.getString("beneficiaryCountry")
            singleTransaction.rechargeAmount =
                singleTransactionJSON.getString("rechargeAmount").toDouble()
            singleTransaction.commissionAmount =
                java.lang.Float.valueOf(singleTransactionJSON.getString("commissionAmount"))
            singleTransaction.createdTime = singleTransactionJSON.getString("createdTime")
            singleTransaction.rechargeStatus = singleTransactionJSON.getString("rechargeStatus")
            singleTransaction.rechargeType = singleTransactionJSON.getString("rechargeType")


            //Set Operator to Service if Operator field in null from API
            if (singleTransactionJSON.isNull("operator")) {
                singleTransaction.operator =
                    singleTransactionJSON.getString("rechargeType")
            } else singleTransaction.operator = singleTransactionJSON.getString("operator")

            if (singleTransaction.rechargeStatus == "SUCCESS") {
                list.add(singleTransaction)
            }

//            val list = listOf("orange", "apple", "apple", "banana", "water", "bread", "banana"

        }

        val count = list.groupingBy { it.rechargeType }.eachCount().filter { it.value >= 1 }
        val listkey = count.keys



        for (i in listkey) {
            var count = 0
            var amount: Double = 0.0
            var commission: Double = 0.0

            for (j in list) {
                if (j.rechargeType == i) {
                    count += 1
                    amount += j.rechargeAmount!!.toDouble()
                    commission += j.commissionAmount!!.toDouble()


                }
            }
            if (i == "Ding") {
                terminallist.add(
                    Data(
                        "Prepaid\nMobile",
                        "%.2f".format(commission),
                        count,
                        "%.2f".format(amount)
                    )
                )
            } else {
                terminallist.add(Data(i!!,"%.2f".format(commission), count, "%.2f".format(amount)))
            }

        }

        val adapter = SalesReportAdapter()
        recycler_view.adapter = adapter
        totallinear.visibility = View.VISIBLE
        print_button.visibility = View.VISIBLE

        adapter.bindData(terminallist)



        if (terminallist.isNotEmpty()) {
            var count = 0
            var amount: Double = 0.0
            var commission: Double = 0.0
            for (i in terminallist) {
                count += i.totalCount
                amount += i.totalMRP.toDouble()
                commission += i.totalCommission.toDouble()

            }
            totalcount.text = "$count"
            totalface_value.text = "${Math.round(amount)}"
            totalcommission.text ="%.2f".format(commission)

            print_button.setOnClickListener {
                if (BluetoothUtil().is_bluetooth_enable) {
                    PrinterFunction(requireContext()).call_sales_report_receipt_printer_bluetooth(
                        "Terminal Saled Report"
                        , terminallist,
                        start,
                        end,
                        "%.2f".format(amount),
                        "%.2f".format(commission)
                    )

                } else enable_bluetooth()

            }


        }

    }

}
package com.example.techorbit.ui.fragment

import android.app.DatePickerDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.adapter.TransactionAdapter
import com.example.techorbit.db.OtarRecharge.OtarRecharge
import com.example.techorbit.model.Data
import com.example.techorbit.model.error.Error
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.ResultOf
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.ui.fragment.live.LiveViewmodel
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.OtarDuViewmodel
import com.example.techorbit.viewmodel.TransactionViewmodel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.*
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.fragment_transaction.progress_circular
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionFragment : Fragment() {
    var start: String? = null
    var end: String? = null
    val TAG = "TAG"
    var totalAmount = ""
    var commisions = ""
    var dblist = ArrayList<Data>()
    var reportList = ArrayList<Data>()
    private val BLUETOOTH_ENABLE_REQUEST = 200
    private lateinit var mCalender: Calendar
    private lateinit var currentDatetime: String
    private val viewmodel: TransactionViewmodel by viewModel()
    private val liveViewmodel: LiveViewmodel by viewModel()
    private lateinit var otarviewmodel: OtarDuViewmodel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repositary =
            TechorbitRepositary(RemoteDataSource().buildRechargeApi(TechorbitClient::class.java))
        val factory = ViewModelFactory(repositary)
        otarviewmodel = ViewModelProvider(this, factory).get(OtarDuViewmodel::class.java)
        mCalender = Calendar.getInstance()
        val monthString =
            DateFormat.format("MMM", Calendar.getInstance().timeInMillis) as String // Jun

        val day =
            DateFormat.format("dd", Calendar.getInstance().timeInMillis) as String // 20

        val year =
            DateFormat.format("yyyy", Calendar.getInstance().timeInMillis) as String // 2013
        currentDatetime = "$day $monthString $year"
        if (isAdded) {
            setupData("$day $monthString $year")
        }
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

            dblist.clear()
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

    fun dateConversion(input: String): String? {
        var output = ""
        if (!input.isEmpty()) {
            try {
                var format = SimpleDateFormat("yyyy-MM-ddTHH:mm:ss", Locale.ENGLISH)
                val newDate = format.parse(input)
                format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                output = format.format(newDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return output
    }

    fun dateLocalConversion(input: String): String? {
        var output = ""
        if (!input.isEmpty()) {
            try {
                var format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                val newDate = format.parse(input)
                format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                output = format.format(newDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return output
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
        getReportData(s)
    }

    private fun getReportData(date: String) {
        if (currentDatetime.trim() == date.trim()) {
            liveViewmodel.dbreportlivedata.observe(
                requireActivity()
            ) {
                if (it.isEmpty()) {
                    if (isOnline(requireContext())) {
                        val head =
                            "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
                        viewmodel.getTransactionReports(head, start!!, end!!)
                    }else {
                        progress_circular.visibility = View.GONE
                        showToast("No Internet")
                    }
                } else {
                    setLocalData(it)
                }
            }
        } else {
            if (isOnline(requireContext())) {
                val head =
                    "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
                viewmodel.getTransactionReports(head, start!!, end!!)
            }else {
                progress_circular.visibility = View.GONE
                showToast("No Internet")
            }
        }
    }


    private fun setLocalData(it: List<OtarRecharge>) {
        dblist.clear()
        val list = ArrayList<Data>()
        var Date = updateLabel()
        var date = dateLocalConversion(Date)
        date = date
        Log.d("date---",date.toString())
        for (i in it!!) {
            val Data = Data()
            Data.beneficiaryCountry = i.beneficiaryCountry
            Data.beneficiaryNumber = i.beneficiaryNumber
            Data.commissionAmount = i.commissionAmount
            Data.createdTime = i.createdTime
            Data.operator = i.operator
            Data.rechargeAmount = i.rechargeAmount
            Data.rechargeId = i.rechargeId
            Data.rechargeStatus = i.rechargeStatus
            Data.rechargeType = i.rechargeType
            Data.type = i.type
            Data.traceId = i.traceId
            var created = Data.createdTime.toString().split('T')
            Log.d("db DATE:::",created[0].toString())
            if (created[0] == date) {
                dblist.add(Data)
                Log.d("db----","entered and added to db")
            }else {
                Log.d("db----","outside and added to db")
//                liveViewmodel.deleteSingleData(Data.traceId.toString())
            }
        }
        if (isOnline(requireContext())) {
            val head =
                "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
            viewmodel.getTransactionReports(head, start!!, end!!)
        }else {
            progress_circular.visibility = View.GONE
            showToast("No Internet")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showData(start: String?, end: String?) {
        if (isOnline(requireContext())) {
            progress_circular.visibility = View.VISIBLE
            val head =
                "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
            viewmodel.getTransactionReports(head, start!!, end!!)
        }else {
            showToast("No Internet")
            progress_circular.visibility = View.GONE
        }
    }

    private fun observeData() {
        viewmodel.liveData.observe(requireActivity(), Observer {
            progress_circular.visibility = View.GONE
            when (it) {
                is ResultOf.Succes -> {
                    doWallet()
                    setData(it.result)
                }
                is ResultOf.Failiure -> {
                    doWallet()
                    if (it.isNetworkError) {
                        showToast(it.message)
                    } else {
                        showToast("Failed")
                    }
                }

            }

        })
    }

    private fun setData(it: Any) {
        reportList.clear()
        var dataList = ArrayList<Data>()
        val v = Gson().toJson(it)
        val response = JSONObject(v)
        val totalJSON: JSONObject = response.getJSONObject("total")
        val rechargeCount: String = totalJSON.getString("rechargeCount").toString()
        recharge_count.setText("$rechargeCount Recharges")
        totalAmount = java.lang.Float.valueOf(totalJSON.getString("rechargeAmount")).toString()
        facevalue.text = "AED $totalAmount"
        commisions = java.lang.Float.valueOf(totalJSON.getString("commissionAmount")).toString()
        commission.text = "AED $commisions"
        val currency = response.getString("currencyAbbreviation")


        val transactionListJSON: JSONArray = response.getJSONArray("data")
        for (i in 0 until transactionListJSON.length()) {
            val singleTransaction = Data()
            val singleTransactionJSON = transactionListJSON.getJSONObject(i)

            if (singleTransactionJSON.getString("type") == "DOWNLOADCARD") {
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

            if (singleTransactionJSON.has("traceid")) {
                singleTransaction.traceId = singleTransactionJSON.getString("traceid")
            } else {
                singleTransaction.traceId = ""
            }
            reportList.add(singleTransaction)
        }
        if (dblist.isNotEmpty()) {
            for (i in 0 until reportList.size) {
                var dataFound = false
                var g = 0
                if (dblist.isNotEmpty()) {
                    for (j in 0 until dblist.size) {
                        g = j
                        if (reportList[i].traceId.toString() == dblist[g].traceId.toString()) {
                            dataFound = true
                            liveViewmodel.deleteSingleData(dblist[g].traceId.toString())
                            break
                        }
                    }
                    if (dataFound) {
                        dblist.remove(dblist[g])
                    }
                    else
                    {
                        if (dataList.isNotEmpty()) {
                            for (k in 0 until dataList.size) {
                                if (dataList.size < dblist.size){
                                    g = dataList.size
                                    if (dataList[k].traceId.toString() != dblist[g].traceId.toString()) {
                                        dataList.add(dblist[g])
                                    }
                                }
                            }
                        }else{
                            dataList.add(dblist[0])
                        }
                    }
                }
            }
            reportList.addAll(0, dataList)
        }
        val adapter = TransactionAdapter { selectitem -> SelctItem(selectitem) }
        tranasction_recyler.adapter = adapter
        adapter.bindData(reportList)
    }

    private fun SelctItem(selectitem: Data) {
        if (selectitem.rechargeStatus.toString().toUpperCase() == "CREATED" ||
            selectitem.rechargeStatus.toString().toUpperCase() == "RECEIVED" ||
            selectitem.rechargeStatus.toString().toUpperCase() == "INITIATED" ||
            selectitem.rechargeStatus.toString().toUpperCase() == "PROCESSING"
        ) {
            progress_circular.visibility = View.VISIBLE
            if (isOnline(requireContext())){
                val terminalId: String? =
                    TechorbitSharedPreference(requireContext()).getValue(KEYS.TERMINALID)
                val header =
                    "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
                val hashMap = HashMap<String, String?>()
                hashMap["traceId"] = selectitem.traceId.toString()
                hashMap["terminalId"] = terminalId
                otarviewmodel.checkRechargeStatus(header, hashMap)
                checkStatus(selectitem)
            }else{
                showToast("No Internet")
            }
        } else {
            val data_list = ArrayList<Data>()
            data_list.add(selectitem)
            if (BluetoothUtil().is_bluetooth_enable()) {
                Log.d("bluetoothUtil", "is enabled")
                PrinterFunction(requireContext()).call_transaction_report_receipt_printer_bluetooth(
                    data_list,
                    start!!,
                    end!!,
                    start!!,
                    end!!,
                    selectitem.rechargeAmount?.toFloat()!!,
                    selectitem.commissionAmount!!
                )
            } else {
                enable_bluetooth()
            }
        }
    }

    private fun checkStatus(item: Data) {
        otarviewmodel.rechargeStatusLiveData.observe(requireActivity(), Observer {
            when (it) {
                is ResultOf.Succes -> {
                    progress_circular.visibility = View.GONE
                    doWallet()
                    if (it.result.response_code == 200 || it.result.response_code == 100) {
                        liveViewmodel.deleteSingleData(item.traceId.toString())
                        dblist.remove(item)
                        showData(start, end)
                    } else if (it.result.response_code == 408 || it.result.response_code == 102) {

                    } else {
                        liveViewmodel.deleteSingleData(item.traceId.toString())
                        dblist.remove(item)
                        showData(start, end)
                    }
                }
                is ResultOf.Failiure -> {
                    val responseCode = it.errorCode
                    progress_circular.visibility = View.GONE
                    doWallet()
                    if (responseCode == 408 || responseCode == 102) {

                    } else {
                        it.errorBody?.let { errorbody ->
                            val body = errorbody
                            val source = body?.source()
                            val buffer = source?.buffer
                            val responcestring =
                                buffer?.clone()?.readString(Charset.forName("UTF-8"))

                            val gson = Gson().fromJson(responcestring, Error::class.java)
                            if (responseCode == 400) {
                                if (gson.error_message == "Recharge not found ") {
                                    liveViewmodel.deleteSingleData(item.traceId.toString())
                                    dblist.remove(item)
                                    showData(start, end)
                                }
                                var invalid = gson.error_message.toString().split(".")
                                if (invalid[0] == "StatusCode: 400 Message(Error 1041) The subscriber MSISDN is invalid") {
                                    liveViewmodel.deleteSingleData(item.traceId.toString())
                                    dblist.remove(item)
                                    showData(start, end)
                                }
                            }
                        }

                    }

                }

            }
        })
    }

    private fun doWallet() {
        val header: String? =
            "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        otarviewmodel.getWallet(header!!)

        otarviewmodel.walletlivedata.observe(requireActivity(), Observer {
            when (it) {
                is ResultOf.Succes -> {

                    TechorbitSharedPreference(requireContext()).save(
                        KEYS.BALANCE,
                        it.result.balance.toString()
                    )
                }
                is ResultOf.Failiure -> {
                    showToast("Something went wrong! check your connection")
                }
            }
        })
    }

    private fun enable_bluetooth() {
        val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(bluetoothIntent, BLUETOOTH_ENABLE_REQUEST)
    }
}
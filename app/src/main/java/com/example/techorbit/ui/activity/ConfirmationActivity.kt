package com.example.techorbit.ui.activity

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.db.OtarRecharge.OtarRecharge
import com.example.techorbit.model.error.Error
import com.example.techorbit.model.otarrecharge.DoOtarRecharge
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.ResultOf
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.ui.fragment.OtarFragment
import com.example.techorbit.ui.fragment.live.LiveViewmodel
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.OtarDuViewmodel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.activity_confirmation.progress_circular
import org.koin.android.viewmodel.ext.android.viewModel
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ConfirmationActivity : AppCompatActivity() {
    private lateinit var viewmodel: OtarDuViewmodel
    lateinit var beneficiaryNumber: String
    lateinit var price: String
    private val BLUETOOTH_ENABLE_REQUEST = 200
    lateinit var operator: String
    lateinit var pending: String
    var dbDate: String = ""
    private lateinit var mCalender: Calendar
    var succes = false
    var backPressed = false
    var traceid: String? = null

    @NonNull
    var id = 0
//    private var otarRechargeList: ArrayList<OtarRecharge>? = null

    private val liveViewmodel: LiveViewmodel by viewModel()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

//        val repositary =
//            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
        val repositary =
            TechorbitRepositary(RemoteDataSource().buildRechargeApi(TechorbitClient::class.java))
        val factory = ViewModelFactory(repositary)
        viewmodel = ViewModelProvider(this, factory).get(OtarDuViewmodel::class.java)
        mCalender = Calendar.getInstance()
        val type = intent.getStringExtra("type")

        val dateTime = Current_DateTime(this).getCurrentDateAndTime() + System.currentTimeMillis()
        dbDate = DateFormat.format("yyyy-MM-ddTHH:mm:ss", mCalender.time) as String
        traceid =
            TechorbitSharedPreference(this).getValue(KEYS.USERNAME) + dateTime + System.currentTimeMillis()
        id = (1 until 1000).random()
        //Todo here we check logic for check status

        checkStatus.setOnClickListener {
            if(isOnline(this.applicationContext)){
                val terminalId: String? = TechorbitSharedPreference(this).getValue(KEYS.TERMINALID)
                val header = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)
                val hashMap = HashMap<String, String?>()
                hashMap["traceId"] = traceid!!
                hashMap["terminalId"] = terminalId
                viewmodel.checkRechargeStatus(header, hashMap)
                progress_circular.visibility = View.VISIBLE
                another.visibility = View.GONE
                checkStatus.visibility = View.GONE
            }else {
                showToast("No Internet")
            }

        }


        type?.let {
            when (it) {
                "du" -> {
                    image.setImageResource(R.drawable.du)
                    title_head.text = "Du"
                    operator = "Du"
                }
                "etisalat" -> {
                    image.setImageResource(R.drawable.etisalat)
                    title_head.text = "Otar"
                    operator = "Otar"
                }
            }
        }

//        getPendingData()

        setUpView()


        confirm.setOnClickListener {
            if(isOnline(this.applicationContext)){
                confirm.isEnabled = false
                cancel.isEnabled = false
//            message.text = ""
                message.visibility = View.GONE
                progress_circular.visibility = View.VISIBLE
                var OtarRecharge = OtarRecharge(
                    beneficiaryNumber = beneficiaryNumber,
                    rechargeAmount = price.toDouble(),
                    type = "RECHARGE",
                    operator = operator.toString(),
                    rechargeId = id,
                    rechargeStatus = "PROCESSING",
                    rechargeType = "Otar",
                    beneficiaryCountry = "UAE",
                    commissionAmount = "0".toFloat(),
                    createdTime = dbDate,
                    traceId = traceid
                )
                liveViewmodel.saveReportData(OtarRecharge)
                getData()
            }else {
                showToast("No Internet")
            }
        }
        cancel.setOnClickListener {
//            backPressed = true
//            onBackPressed()
            finish()
        }

        another.setOnClickListener {
            succes = true
            onBackPressed()
        }
        tryagain.setOnClickListener {
            if(isOnline(this.applicationContext)){
                progress_circular.visibility = View.VISIBLE
                getData()
            }else {
                showToast("No Internet")
            }
        }


        observeData()


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

    private fun setUpView() {
        val intent = intent
        price = intent.getStringExtra("amount").toString()
        beneficiaryNumber = intent.getStringExtra("beneficiaryNumber").toString()
        beneficiryno.setText(beneficiaryNumber)
        pending = intent.getStringExtra("pending").toString()
        Log.d("pending", pending)

        amount.text = "AED $price"

        if (pending == "Y") {
            confirm.isEnabled = false;
            message.visibility = View.VISIBLE
            message.text = "This number is already pending \n See transaction report"
        } else {
            message.visibility = View.GONE
        }


//        Log.d("TAG", "$beneficiaryNumber")
        val boolean = isZeroPrefix(beneficiaryNumber)
        if (boolean) {
            val number = removeZero(beneficiaryNumber)
            beneficiaryNumber = number

        }

//        getPendingData()

    }

    private fun observeData() {

        //Todo this live returns responce from Retrieve api

        viewmodel.rechargeStatusLiveData.observe(this, Observer {
            when (it) {
                is ResultOf.Succes -> {
                    progress_circular.visibility = View.GONE
                    doWallet()
//                    Log.d("status----", it.result.status.toString())
                    if (it.result.response_code == 200 ||it.result.response_code == 100 ) {
//                        doWallet()
                        liveViewmodel.deleteSingleData(traceid.toString())
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.VISIBLE
                        status_image.setImageResource(R.drawable.success)
                        status_text.also { view ->
                            view.text = "Recharge Success"
                            view.setTextColor(resources.getColor(R.color.green))
                        }
//                        report.text = "Failed"
                        status_text.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        another.visibility = View.GONE
                        tryagain.visibility = View.GONE
                        checkStatus.visibility = View.GONE
                        message.visibility = View.GONE
                        setPrint(
                            DoOtarRecharge(
                                it.result.error_message,
                                it.result.response_code,
                                it.result.status,
                                it.result.rechargeId
                            )
                        )
                    }
                    else if(it.result.response_code == 408 || it.result.response_code == 102) {
                        status_image.setImageResource(R.drawable.ic_baseline_pending_24)
                        status_text.also { view ->
                            view.text = "Recharge Processing"
                            view.setTextColor(resources.getColor(R.color.orange))
                        }
                        report.visibility = View.GONE
                        checkStatus.visibility = View.VISIBLE
//                        report.text = "Failed"
                        report_error_message.visibility = View.GONE
                        report_error_message.text = it.result.error_message.toString()
                        message.visibility = View.GONE
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.GONE
                        another.visibility = View.VISIBLE
                        status_text.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        Toast.makeText(this, "Timeout...", Toast.LENGTH_LONG).show()
                    }
                    else{
                        liveViewmodel.deleteSingleData(traceid.toString())
                        status_image.setImageResource(R.drawable.failed)
                        status_text.also { view ->
                            view.text = "Recharge Failed"
                            view.setTextColor(resources.getColor(R.color.red))
                        }
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.GONE
                        checkStatus.visibility = View.GONE
                        another.visibility = View.VISIBLE
                        status_text.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        message.visibility = View.GONE
                        report.visibility = View.VISIBLE
                        report.text = it.result.error_message.toString()
                    }
                }
                is ResultOf.Failiure -> {
                    var responseCode = it.errorCode
                    progress_circular.visibility = View.GONE
                    linear_confirmation.visibility = View.GONE
                    linear_success.visibility = View.GONE
                    another.visibility = View.VISIBLE
                    confirm.visibility = View.GONE
                    cancel.visibility = View.GONE
                    doWallet()
                    if (responseCode == 408 || responseCode == 102){
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.GONE
                        message.visibility = View.GONE
                        status_text.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        checkStatus.visibility = View.VISIBLE
                        status_image.setImageResource(R.drawable.ic_baseline_pending_24)
                        status_text.also { view ->
                            view.text = "Recharge Processing"
                            view.setTextColor(resources.getColor(R.color.orange))
                        }
                    }
                    else if(responseCode == 401){
                        liveViewmodel.deleteSingleData(traceid.toString())
                        val username = TechorbitSharedPreference(this).getValue(KEYS.USER)
                        val password = TechorbitSharedPreference(this).getValue(KEYS.PASS)
                        val deviceid = TechorbitSharedPreference(this).getValue(KEYS.DEVICEID)
                        username?.let {
                            viewmodel.signup(username, password!!, deviceid)
                        }


                        viewmodel.data.observe(this, Observer {
                            when (it) {
                                is ResultOf.Succes -> {
                                    message.visibility = View.GONE
                                    progress_circular.visibility = View.GONE
                                    linear_confirmation.visibility = View.GONE
                                    linear_success.visibility = View.GONE
                                    another.visibility = View.GONE
                                    status_text.visibility = View.GONE
                                    status_image.visibility = View.GONE
                                    tryagain.visibility = View.VISIBLE
                                    saveData(it)
                                }
                                is ResultOf.Failiure -> {
                                    message.visibility = View.GONE
                                    progress_circular.visibility = View.GONE
                                    linear_confirmation.visibility = View.GONE
                                    linear_success.visibility = View.GONE
                                    another.visibility = View.GONE
                                    status_text.visibility = View.GONE
                                    message.visibility = View.GONE
                                    message.text = ""
                                    status_image.visibility = View.GONE
                                    tryagain.visibility = View.VISIBLE
                                }
                            }
                        })

                    }
                    else {
                        it.errorBody?.let { errorbody ->
                            val body = errorbody
                            val source = body?.source()
                            val buffer = source?.buffer
                            val responcestring =
                                buffer?.clone()?.readString(Charset.forName("UTF-8"))
                            val gson = Gson().fromJson(responcestring, Error::class.java)
                            liveViewmodel.deleteSingleData(traceid.toString())
                            status_text.visibility = View.VISIBLE
                            status_image.visibility = View.VISIBLE
                            status_image.setImageResource(R.drawable.failed)
                            status_text.also { view ->
                                view.text = "Recharge Failed"
                                view.setTextColor(resources.getColor(R.color.red))
                            }
                            showToast(gson.error_message!!)
                            report_error_message.visibility = View.VISIBLE
                            report.visibility = View.VISIBLE
                            checkStatus.visibility = View.GONE
                            report_error_message.text = "${gson.error_message}"
                            report.text = "Failed-${gson.sub_message}"

                        }
                    }

                }
            }
        })


        viewmodel.liveData.observe(this, Observer {
            when (it) {
                is ResultOf.Succes -> {
                    progress_circular.visibility = View.GONE

                    //Todo here will check the responce body
                    if (it.result.response_code == 400) {
                        doWallet()
                        status_image.setImageResource(R.drawable.failed)
                        status_text.also { view ->
                            view.text = "Recharge Processing"
                            view.setTextColor(resources.getColor(R.color.red))
                        }
                        report.visibility = View.GONE
                        checkStatus.visibility = View.VISIBLE
                        report.text = "Failed"
                        report_error_message.visibility = View.GONE
                        report_error_message.text = it.result.sub_message.toString()
                        message.visibility = View.GONE
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.GONE
                        another.visibility = View.GONE
                        status_text.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        Toast.makeText(this, "${it.result.error_message}", Toast.LENGTH_LONG).show()
                        var OtarRecharge = OtarRecharge(
                            beneficiaryNumber = beneficiaryNumber,
                            rechargeAmount = price.toDouble(),
                            type = "RECHARGE",
                            operator = operator.toString(),
                            rechargeId = id,
                            rechargeStatus = "PROCESSING",
                            rechargeType = "Otar",
                            beneficiaryCountry = "UAE",
                            commissionAmount = "0".toFloat(),
                            createdTime = dbDate,
                            traceId = traceid
                        )
                        liveViewmodel.saveReportData(OtarRecharge)
                    } else {

                        succes = true
                        doWallet()
                        liveViewmodel.deleteSingleData(traceid.toString())
                        message.visibility = View.GONE
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.VISIBLE
                        status_image.setImageResource(R.drawable.success)
                        status_text.also { view ->
                            view.text = "Recharge Success"
                            view.setTextColor(resources.getColor(R.color.green))
                        }
                        status_text.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        another.visibility = View.GONE
                        tryagain.visibility = View.GONE

                        setPrint(it.result)

                    }

                }
                is ResultOf.Failiure -> {
                    message.visibility = View.GONE
                    doWallet()
                    val responceCode = it.errorCode
                    Log.d("errorCode--", responceCode.toString())
                    progress_circular.visibility = View.GONE
                    linear_confirmation.visibility = View.GONE
                    linear_success.visibility = View.GONE
                    another.visibility = View.VISIBLE
                    confirm.visibility = View.GONE
                    cancel.visibility = View.GONE
                    if (responceCode == 408 || responceCode == 102) {
//                    showToast("Timeout!...Connection error")
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.GONE
                        message.visibility = View.GONE
                        status_text.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        checkStatus.visibility = View.VISIBLE
                        status_image.setImageResource(R.drawable.ic_baseline_pending_24)
                        status_text.also { view ->
                            view.text = "Recharge Processing"
                            view.setTextColor(resources.getColor(R.color.orange))
                        }
                    } else if (responceCode == 401) {
                        liveViewmodel.deleteSingleData(traceid.toString())
                        val username = TechorbitSharedPreference(this).getValue(KEYS.USER)
                        val password = TechorbitSharedPreference(this).getValue(KEYS.PASS)
                        val deviceid = TechorbitSharedPreference(this).getValue(KEYS.DEVICEID)
                        username?.let {
                            viewmodel.signup(username, password!!, deviceid)
                        }


                        viewmodel.data.observe(this, Observer {
                            when (it) {
                                is ResultOf.Succes -> {
                                    message.visibility = View.GONE
                                    progress_circular.visibility = View.GONE
                                    linear_confirmation.visibility = View.GONE
                                    linear_success.visibility = View.GONE
                                    another.visibility = View.GONE
                                    status_text.visibility = View.GONE
                                    status_image.visibility = View.GONE
                                    tryagain.visibility = View.VISIBLE
                                    saveData(it)
                                }
                                is ResultOf.Failiure -> {
                                    message.visibility = View.GONE
                                    progress_circular.visibility = View.GONE
                                    linear_confirmation.visibility = View.GONE
                                    linear_success.visibility = View.GONE
                                    another.visibility = View.GONE
                                    status_text.visibility = View.GONE
                                    message.visibility = View.GONE
                                    message.text = ""
                                    status_image.visibility = View.GONE
                                    tryagain.visibility = View.VISIBLE
                                }
                            }
                        })


                    } else {
                        it.errorBody?.let { errorbody ->
                            val body = errorbody
                            val source = body?.source()
                            val buffer = source?.buffer
                            val responcestring =
                                buffer?.clone()?.readString(Charset.forName("UTF-8"))
                            val gson = Gson().fromJson(responcestring, Error::class.java)

                            status_text.visibility = View.VISIBLE
                            status_image.visibility = View.VISIBLE
                            status_image.setImageResource(R.drawable.failed)
                            status_text.also { view ->
                                view.text = "Recharge Failed"
                                view.setTextColor(resources.getColor(R.color.red))
                            }
                            liveViewmodel.deleteSingleData(traceid.toString())
                            showToast(gson.error_message!!)

                            report_error_message.visibility = View.VISIBLE
                            report.visibility = View.VISIBLE
                            checkStatus.visibility = View.GONE
                            report_error_message.text = "${gson.error_message}"
                            report.text = "Failed-${gson.sub_message}"

//
                        }
                    }
                }
            }
        })
    }

//    private fun rechargeStatus(it:)

    private fun openDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Timeout.")
            .setMessage("Please check Transaction Report")
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                finish()
                dialog.dismiss()
            }.show()
    }

    private fun saveData(it: ResultOf.Succes<SignupData>) {
        TechorbitSharedPreference(this).save(KEYS.TOKEN, it.result.token)
    }

    private fun doWallet() {
        val header: String? = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)
        viewmodel.getWallet(header!!)

        viewmodel.walletlivedata.observe(this, Observer {
            when (it) {
                is ResultOf.Succes -> {

                    TechorbitSharedPreference(this).save(KEYS.BALANCE, it.result.balance.toString())
                }
                is ResultOf.Failiure -> {
                    showToast("Something went wrong! check your connection")
                }
            }
        })
    }

    private fun setPrint(recharge: DoOtarRecharge) {

        reprint.setOnClickListener {
            if (BluetoothUtil().is_bluetooth_enable()) {
                PrinterFunction(this).call_OTAR_receipt_printer_bluetooth(
                    recharge.rechargeId.toString(),
                    beneficiaryNumber,
                    "AED $price", operator,
                    "SUCCESS"
                )
            } else {
                beneficiaryNumber
                enable_bluetooth()
            }

        }
        success.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun getData() {
        val intent = intent
        val deviceid = TechorbitSharedPreference(this).getValue(KEYS.DEVICEID)
        val deviceKey = TechorbitSharedPreference(this).getValue(KEYS.DEVICEKEY)
        val type = intent.getStringExtra("type")
        val hashMap = HashMap<String, String>()
        val header = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)

        hashMap.put("beneficiaryNumber", beneficiaryNumber)
        hashMap.put("amount", price)
        hashMap.put("deviceId", deviceid.toString())
        hashMap.put("deviceKey", deviceKey.toString())
        hashMap.put("type", type.toString())

        val headerMap = HashMap<String, String>()
        headerMap["Authorization"] = header
        headerMap["TraceId"] = traceid!!
        viewmodel.saveRecharge(headerMap, hashMap)
    }

    private fun getPendingData() {
        liveViewmodel.dbreportlivedata.observe(this, Observer {
            if (it.isNotEmpty()) {
                for (i in it!!) {
                    if (beneficiaryNumber == i.beneficiaryNumber.toString()) {
                        message.visibility = View.VISIBLE
                        message.text = "This number is already in pending \n See transaction report"
                        confirm.isEnabled = false
                    } else {
                    }
                }
            } else {
                message.visibility = View.GONE
                message.text = ""
            }
        })
    }

    private fun enable_bluetooth() {
        val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(bluetoothIntent, BLUETOOTH_ENABLE_REQUEST)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (succes) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            startActivity(intent)
        }
    }
}
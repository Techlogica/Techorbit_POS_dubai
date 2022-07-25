package com.example.techorbit.ui.activity

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.model.error.Error
import com.example.techorbit.model.otarrecharge.DoOtarRecharge
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.ResultOf
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.InternationalRechargeViewmodel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.*
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.amount
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.another
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.confirm
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.linear_confirmation
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.linear_success
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.progress_circular
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.status_image
import kotlinx.android.synthetic.main.activity_international_recharge_confirmation.status_text
import java.nio.charset.Charset
import java.util.*

class InternationalRechargeConfirmation : AppCompatActivity() {


    private lateinit var viewmodel: InternationalRechargeViewmodel
    private val BLUETOOTH_ENABLE_REQUEST = 200;
    var success = false
    var aedAmount = "0"
    var traceid: String? = null;

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_international_recharge_confirmation)

        val repositary =
            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
        val factory = ViewModelFactory(repositary)
        viewmodel = ViewModelProvider(this, factory).get(InternationalRechargeViewmodel::class.java)
        setData()

        cancel_button.setOnClickListener {
            finish()
        }

        success_button.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        val dateTime = Current_DateTime(this).getCurrentDateAndTime()
        traceid =
            TechorbitSharedPreference(this).getValue(KEYS.USERNAME) + dateTime + System.currentTimeMillis()
        checkStatusInternational.setOnClickListener {
            val terminalId: String? = TechorbitSharedPreference(this).getValue(KEYS.TERMINALID)
            val header = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)
            val hashMap = HashMap<String, String?>()
            hashMap["traceId"] = traceid!!
            hashMap["terminalId"] = terminalId
            viewmodel.checkRechargeStatus(header, hashMap)
            progress_circular.visibility = View.VISIBLE
        }


    }

    private fun setData() {

//        progressDialog = ProgressDialog(this)
        val beneficiaryNumber = intent.getStringExtra("beneficiaryNumber")
        val denominationId = intent.getStringExtra("denominationId")
        val operatorName = intent.getStringExtra("operatorName")
        val deviceKey = intent.getStringExtra("deviceKey")
        val deviceId = intent.getStringExtra("deviceId")
        val extension = intent.getStringExtra("extension")
        val mrp = intent.getStringExtra("mrp")
        val rechargeAmount = intent.getStringExtra("rechargeAmount")
        val localCurrency = intent.getStringExtra("localCurrency")
        val beneficiaryCountryName = intent.getStringExtra("beneficiaryCountryName")

        val filteruserPrice = intent.getStringExtra("filteruserPrice")
        val useramount = intent.getStringExtra("useramount")


        number.text = beneficiaryNumber
        operator_name.text = operatorName

        uae_amount.text = "AED $mrp"

        if (filteruserPrice != null) {
            uae_amount.text = "AED $useramount"
            amount.text = "$localCurrency $filteruserPrice"
            aedAmount = useramount.toString()
        } else {
            uae_amount.text = "AED $mrp"
            amount.text = "$localCurrency $rechargeAmount"
        }
        another.setOnClickListener {
            finish()
        }

        confirm.setOnClickListener {
//            progressDialog.show()
            confirm.isEnabled = false
            cancel_button.isEnabled = false
            confirmation_text.visibility = View.GONE
            progress_circular.visibility = View.VISIBLE
            val hashMap = HashMap<String, String>()
            hashMap["beneficiaryNumber"] = "${extension.toString()}${beneficiaryNumber.toString()}"
            hashMap["denominationId"] = denominationId.toString()
            hashMap["operatorName"] = operatorName.toString()
            hashMap["deviceKey"] = deviceKey.toString()
            hashMap["deviceId"] = deviceId.toString()
            hashMap["amount"] = aedAmount
            val header = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)

            // Todo Here we coment and uncomment trace id: Line 139

            val headerMap = HashMap<String, String>()
            headerMap["Authorization"] = header
            headerMap["TraceId"] = traceid!!

            viewmodel.interNationalRecharge(headerMap, hashMap)

        }



        tryrecharge.setOnClickListener {
            progress_circular.visibility = View.VISIBLE
            val hashMap = HashMap<String, String>()
            hashMap["beneficiaryNumber"] = "${extension.toString()}${beneficiaryNumber.toString()}"
            hashMap["denominationId"] = denominationId.toString()
            hashMap["operatorName"] = operatorName.toString()
            hashMap["deviceKey"] = deviceKey.toString()
            hashMap["deviceId"] = deviceId.toString()
            hashMap["amount"] = aedAmount
            val header = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)
            val headerMap = HashMap<String, String>()
            headerMap["Authorization"] = header
            headerMap["TraceId"] = traceid!!
            viewmodel.interNationalRecharge(headerMap, hashMap)


        }

        viewmodel.liveData.observe(this, Observer { result ->

//            Log.d("TAG", "setData: $result ")

            when (result) {
                is ResultOf.Succes -> {
                    if (result.result.response_code == 400) {
                        status_image.setImageResource(R.drawable.failed)
                        status_text.also { view ->
                            view.text = "Recharge Failed"
                            view.setTextColor(resources.getColor(R.color.red))
                        }



                        confirmation_text.visibility = View.GONE
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.GONE
                        another.visibility = View.VISIBLE
                        tryrecharge.visibility = View.GONE
                        status_text.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        showToast("Recharge Failed")
                    } else {

                        success = true
                        progress_circular.visibility = View.GONE
                        doWallet()
                        confirmation_text.visibility = View.GONE
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
                        tryrecharge.visibility = View.GONE
                        checkStatusInternational.visibility=View.GONE

                        success_button.setOnClickListener {
                            finish()
                        }
                        print.setOnClickListener {
                            if (BluetoothUtil().is_bluetooth_enable()) {
                                PrinterFunction(this).call_recharge_receipt_printer_bluetooth(
                                    "${result.result.rechargeId}",
                                    beneficiaryNumber!!, "${amount.text}",
                                    "${uae_amount.text}",
                                    "Success",
                                    beneficiaryCountryName!!
                                )
                            } else {
                                enable_bluetooth()
                            }

                            val intent = Intent(this, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                }
                is ResultOf.Failiure -> {
                    confirm.isEnabled = true
                    cancel_button.isEnabled = true
                    val responceCode = result.errorCode
                    showToast("$responceCode")
                    if (responceCode == 400) {
                        progress_circular.visibility = View.GONE

                        result.errorBody?.let { errorbody ->
                            val body = errorbody
                            val source = body?.source()
                            val buffer = source?.buffer
                            val responcestring =
                                buffer?.clone()?.readString(Charset.forName("UTF-8"))

                            val gson = Gson().fromJson(responcestring, Error::class.java)
                            confirmation_text.visibility = View.VISIBLE
                            confirmation_text.text = gson.sub_message

                            val text = " Failed by an result from API"
                            if (gson.sub_message.equals(text)) {
                                openDialog()
                            } else {

                                status_image.setImageResource(R.drawable.failed)
                                status_text.also { view ->
                                    view.text = "Recharge Failed"
                                    view.setTextColor(resources.getColor(R.color.red))
                                }

                                linear_confirmation.visibility = View.GONE
                                linear_success.visibility = View.GONE
                                tryrecharge.visibility = View.GONE
                                another.visibility = View.VISIBLE

                                status_text.visibility = View.VISIBLE
                                status_image.visibility = View.VISIBLE


                            }
                            showToast(gson.error_message!!)

                        }


                    } else if (responceCode == 408) {
                        progress_circular.visibility = View.GONE
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.GONE
                        tryrecharge.visibility = View.GONE
                        another.visibility = View.GONE
                        checkStatusInternational.visibility = View.VISIBLE


                    } else if (responceCode == 401) {

                        val username = TechorbitSharedPreference(this).getValue(KEYS.USER)
                        val password = TechorbitSharedPreference(this).getValue(KEYS.PASS)
                        val deviceid = TechorbitSharedPreference(this).getValue(KEYS.DEVICEID)
                        username?.let {
                            viewmodel.signup(username, password!!, deviceid)
                        }
                        viewmodel.data.observe(this, Observer {
                            when (it) {
                                is ResultOf.Succes -> {
                                    progress_circular.visibility = View.GONE
                                    linear_confirmation.visibility = View.GONE
                                    another.visibility = View.GONE
                                    tryrecharge.visibility = View.VISIBLE


                                    saveData(it)
                                }
                                is ResultOf.Failiure -> {
                                    progress_circular.visibility = View.GONE
                                    linear_confirmation.visibility = View.GONE
                                    another.visibility = View.GONE
                                    tryrecharge.visibility = View.VISIBLE


                                }
                            }
                        })

                    } else if (result.isNetworkError) {
                        confirmation_text.visibility = View.VISIBLE
                        confirmation_text.text = "No Network"
                        progress_circular.visibility = View.GONE
                        showToast("No Network")
                    } else {
                        progress_circular.visibility = View.GONE
                    }


                }
            }
        })

        viewmodel.rechargeStatusLiveData.observe(this, Observer {result->
            when (result) {
                is ResultOf.Succes -> {
                    success = true
                    progress_circular.visibility = View.GONE
                    doWallet()
                    confirmation_text.visibility = View.GONE
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
                    tryrecharge.visibility = View.GONE
                    checkStatusInternational.visibility= View.GONE

                    success_button.setOnClickListener {
                        finish()
                    }
                    print.setOnClickListener {
                        if (BluetoothUtil().is_bluetooth_enable()) {
                            PrinterFunction(this).call_recharge_receipt_printer_bluetooth(
                                "${result.result.rechargeId}",
                                beneficiaryNumber!!, "${amount.text}",
                            "${uae_amount.text}",
                                "Success",
                                beneficiaryCountryName!!
                            )
                        } else {
                            enable_bluetooth()
                        }

                        val intent = Intent(this, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

                }
                is ResultOf.Failiure -> {
                    progress_circular.visibility = View.GONE
                    status_image.setImageResource(R.drawable.failed)
                    status_text.also { view ->
                        view.text = "Recharge Failed"
                        view.setTextColor(resources.getColor(R.color.red))
                    }

                    linear_confirmation.visibility = View.GONE
                    linear_success.visibility = View.GONE
                    checkStatusInternational.visibility=View.GONE
                    another.visibility = View.VISIBLE
                    status_text.visibility = View.VISIBLE
                    status_image.visibility = View.VISIBLE

                    result.errorBody?.let { errorbody ->
                        val body = errorbody
                        val source = body?.source()
                        val buffer = source?.buffer
                        val responcestring =
                            buffer?.clone()?.readString(Charset.forName("UTF-8"))

                        val gson = Gson().fromJson(responcestring, Error::class.java)
                        confirmation_text.visibility = View.VISIBLE
                        confirmation_text.text = gson.sub_message

                    }


                }
            }
        })


    }

    private fun openDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Timeout.")
            .setMessage("Please check Transaction Report")
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                finish()
                dialog.dismiss()
            }

            .show()
    }

    private fun saveData(it: ResultOf.Succes<SignupData>) {
        TechorbitSharedPreference(this).save(KEYS.TOKEN, it.result.token)
    }

    private fun enable_bluetooth() {
        val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(bluetoothIntent, BLUETOOTH_ENABLE_REQUEST)
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (success) {
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
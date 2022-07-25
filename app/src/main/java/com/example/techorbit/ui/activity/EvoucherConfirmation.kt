package com.example.techorbit.ui.activity

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.techorbit.R
import com.example.techorbit.model.Plans
import com.example.techorbit.model.error.Error
import com.example.techorbit.model.scratchcardrecharge.Data
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.ScratchCardViewmodel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.*
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.cancel
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.confirm
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.image
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.linear_confirmation
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.linear_success
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.progress_circular
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.reprint
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.status_image
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.title_head
import org.koin.android.viewmodel.ext.android.viewModel
import java.nio.charset.Charset

class EvoucherConfirmation : AppCompatActivity() {

    private val viewmodel: ScratchCardViewmodel by viewModel()
    private var counts = ""
    private var transId = ""
    private var transCode = ""
    var rechargeCardNumber_decode1 = ""
    var decoded_rechargeCardNumber: String? = null
    private var logo: Int? = null
    var mrp = ""
    var traceid: String? = null;


    private var data: List<Data>? = null;

    var success = false
    private val BLUETOOTH_ENABLE_REQUEST = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evoucher_confirmation)

        val dateTime = Current_DateTime(this).getCurrentDateAndTime()
        traceid =
            TechorbitSharedPreference(this).getValue(KEYS.USERNAME) + dateTime + System.currentTimeMillis()

        cancel.setOnClickListener {
            finish()
        }
        setupData()

        observeData()

        checkEvoucherStatus.setOnClickListener {
            val terminalId: String? = TechorbitSharedPreference(this).getValue(KEYS.TERMINALID)
            val header = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)
            val hashMap = HashMap<String, String?>()
            hashMap["traceId"] = traceid!!
            hashMap["terminalId"] = terminalId
            viewmodel.checkRechargeStatus(header, hashMap)
        }
    }

    private fun observeData() {

        viewmodel.rechargeStatusLiveData.observe(this, Observer {

            when(it){
                is ResultOf.Succes->{
                    progress_circular.visibility = View.GONE
                    success = true
                    doWallet()
                    linear_confirmation.visibility = View.GONE
                    linear_success.visibility = View.VISIBLE
                    status_image.visibility = View.VISIBLE
                    checkEvoucherStatus.visibility = View.GONE
                    status_image.setImageResource(R.drawable.success)
                    status_head.also { view ->
                        view.text = "Recharge Success"
                        view.setTextColor(resources.getColor(R.color.green))
                    }
                    data = it.result.data
                    for (i in data!!) {
                        transId = i.transactionId.toString()
                        transCode = i.transactionCode
                        rechargeCardNumber_decode1 = decode_base64(i.rechargeCardNumber)!!
                        decoded_rechargeCardNumber =
                            decode_base64(rechargeCardNumber_decode1!!)




                        if (BluetoothUtil().is_bluetooth_enable()) {
                            logo?.let {
                                logo_print(it)
                            }
                            PrinterFunction(this).call_scratchcaerd_printer_bluetooth(
                                scrach_card_name.text.toString(),
                                "AED",
                                mrp + "",
                                "beneficiaryNumber",
                                decoded_rechargeCardNumber!!,
                                transId,
                                transCode, ""
                            )
                        } else {
                            enable_bluetooth()
                        }


                    }

                }
                else -> {
                    progress_circular.visibility = View.GONE
                    status_image.visibility = View.VISIBLE
                    linear_confirmation.visibility = View.GONE
                    checkEvoucherStatus.visibility = View.GONE
                    status_image.setImageResource(R.drawable.failed)
                    status_head.also { view ->
                        view.text = "Recharge Failed"
                        view.setTextColor(resources.getColor(R.color.red))
                    }

                    another_button.visibility = View.VISIBLE
                    tryevoucher.visibility = View.GONE
                    error_message.visibility = View.VISIBLE
//                    error_message.text = it.re.sub_message
//                    showToast(it.result.error_message)
                }
            }

        })

        viewmodel.liveData.observe(this, Observer {
            when (it) {
                is ResultOf.Succes -> {


                    if (it.result.response_code == 100) {
                        progress_circular.visibility = View.GONE
                        success = true
                        doWallet()
                        linear_confirmation.visibility = View.GONE
                        linear_success.visibility = View.VISIBLE
                        status_image.visibility = View.VISIBLE
                        checkEvoucherStatus.visibility = View.GONE
                        status_image.setImageResource(R.drawable.success)
                        status_head.also { view ->
                            view.text = "Recharge Success"
                            view.setTextColor(resources.getColor(R.color.green))
                        }
                        data = it.result.data
                        for (i in data!!) {
                            transId = i.transactionId.toString()
                            transCode = i.transactionCode
                            rechargeCardNumber_decode1 = decode_base64(i.rechargeCardNumber)!!
                            decoded_rechargeCardNumber =
                                decode_base64(rechargeCardNumber_decode1!!)




                            if (BluetoothUtil().is_bluetooth_enable()) {
                                logo?.let {
                                    logo_print(it)
                                }
                                PrinterFunction(this).call_scratchcaerd_printer_bluetooth(
                                    scrach_card_name.text.toString(),
                                    "AED",
                                    mrp + "",
                                    "beneficiaryNumber",
                                    decoded_rechargeCardNumber!!,
                                    transId,
                                    transCode, ""
                                )
                            } else {
                                enable_bluetooth()
                            }


                        }


                    } else {
                        progress_circular.visibility = View.GONE
                        status_image.visibility = View.VISIBLE
                        linear_confirmation.visibility = View.GONE
                        checkEvoucherStatus.visibility = View.GONE
                        status_image.setImageResource(R.drawable.failed)
                        status_head.also { view ->
                            view.text = "Recharge Failed"
                            view.setTextColor(resources.getColor(R.color.red))
                        }
//                            another.visibility = View.VISIBLE

                        another_button.visibility = View.VISIBLE
                        tryevoucher.visibility = View.GONE
                        error_message.visibility = View.VISIBLE
                        error_message.text = it.result.sub_message

                        showToast(it.result.error_message)


                    }

                }
                is ResultOf.Failiure -> {

                    val responceCode = it.errorCode


                    if (responceCode == 401) {
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
                                    another_button.visibility = View.GONE
                                    tryevoucher.visibility = View.VISIBLE
                                    checkEvoucherStatus.visibility = View.GONE


                                    saveData(it)
                                }
                                is ResultOf.Failiure -> {
                                    progress_circular.visibility = View.GONE
                                    linear_confirmation.visibility = View.GONE
                                    another_button.visibility = View.GONE
                                    tryevoucher.visibility = View.VISIBLE
                                    checkEvoucherStatus.visibility = View.GONE


                                }
                            }
                        })


                    } else if (responceCode == 408) {

                        progress_circular.visibility = View.GONE
                        linear_confirmation.visibility = View.GONE

                        another_button.visibility = View.GONE
                        tryevoucher.visibility = View.GONE
                        checkEvoucherStatus.visibility = View.VISIBLE


                    } else if (it.isNetworkError) {
                        progress_circular.visibility = View.GONE
                        showToast("No internet")
                        cancel.isEnabled = true

                    } else {
                        showToast("Something went wrong")
                    }


                }
            }
        })
    }

    private fun saveData(it: ResultOf.Succes<SignupData>) {
        TechorbitSharedPreference(this).save(KEYS.TOKEN, it.result.token)
    }

    private fun setupData() {
        val head = intent.getStringExtra("head")
        val value = intent.getParcelableExtra<Plans>("value")
        counts = intent.getStringExtra("count").toString()
        mrp = value?.mrp.toString()
        count.text = counts
        face_value.text = "AED" + value?.mrp


        when (head) {

            "0" -> {
                title_head.text = "Du Voucher"
                scrach_card_name.text = "Du Voucher"
                image.setImageResource(R.drawable.du)
                logo = R.drawable.du_print

            }

            "1" -> {
                title_head.text = "Etisalat E-voucher"
                scrach_card_name.text = "Etisalat E-voucher"
                image.setImageResource(R.drawable.etisalat)
                logo = R.drawable.etisalat_print
            }
            "2" -> {
                scrach_card_name.text = "Five E-Voucher"
                title_head.text = "Five E-Voucher"
                image.setImageResource(R.drawable.five)
                logo = R.drawable.five_print
            }
            "3" -> {
                scrach_card_name.text = "Hello E-Voucher"
                title_head.text = "HelloE-Voucher"
                image.setImageResource(R.drawable.hello)
                logo = R.drawable.hello_print
            }
            "4" -> {
                scrach_card_name.text = "Salik E-Voucher"
                title_head.text = "SalikE-Voucher"
                image.setImageResource(R.drawable.salik)
                logo = R.drawable.salik_print
            }
            "5" -> {
                scrach_card_name.text = "PlayStation E-Voucher"
                title_head.text = "PlayStation E-Voucher"
                image.setImageResource(R.drawable.ic_play_station)
                logo = R.drawable.play_print
            }

            "6" -> {

                scrach_card_name.text = "Virigin E-Voucher"
                title_head.text = "Virigin E-Voucher"
                image.setImageResource(R.drawable.virgin_mobiles)
                logo = R.drawable.virgin_mobiles_print
            }
            "7" -> {

                scrach_card_name.text = "Xbox E-Voucher"
                title_head.text = "Xbox E-Voucher"
                image.setImageResource(R.drawable.xbox)
                logo = R.drawable.xbox_print
            }
            "8" -> {

                scrach_card_name.text = "Itunes E-Voucher"
                title_head.text = "Itunes E-Voucher"
                image.setImageResource(R.drawable.itunes)
                logo = R.drawable.itunes_print
            }
            "9" -> {

                scrach_card_name.text = "Pubg E-Voucher"
                title_head.text = "Pubg E-Voucher"
                image.setImageResource(R.drawable.pubg)
                logo = R.drawable.pubg_print
            }
            "12" -> {

                scrach_card_name.text = "G-Play E-Voucher"
                title_head.text = "G-Play E-Voucher"
                image.setImageResource(R.drawable.play)
                logo = R.drawable.g_print
            }
            "14" -> {

                scrach_card_name.text = "FreeFire E-Voucher"
                title_head.text = "FreeFire E-Voucher"
                image.setImageResource(R.drawable.freefire)
                logo = R.drawable.free
            }
            "15" -> {

                scrach_card_name.text = "Xbox-US E-Voucher"
                title_head.text = "Xbox-US E-Voucher"
                image.setImageResource(R.drawable.xbox)
                logo = R.drawable.xbox_print
            }
            "16" -> {

                scrach_card_name.text = "Playstation-US E-Voucher"
                title_head.text = "Playstation-US E-Voucher"
                image.setImageResource(R.drawable.ic_play_station)
                logo = R.drawable.play_print
            }
            "17" -> {

                scrach_card_name.text = "Playstation-Plus-UAE E-Voucher"
                title_head.text = "Playstation-Plus-UAE E-Voucher"
                image.setImageResource(R.drawable.ic_play_station)
                logo = R.drawable.play_print
            }

            "18" -> {

                scrach_card_name.text = "Playstation-Plus-US E-Voucher"
                title_head.text = "Playstation-Plus-US E-Voucher"
                image.setImageResource(R.drawable.ic_play_station)
                logo = R.drawable.play_print
            }


            "19" -> {

                scrach_card_name.text = "Itunes-US E-Voucher"
                title_head.text = "Itunes-US E-Voucher"
                image.setImageResource(R.drawable.itunes)
                logo = R.drawable.itunes_print
            }

            "20" -> {

                scrach_card_name.text = "GooglePlay-US E-Voucher"
                title_head.text = "GooglePlay-US E-Voucher"
                image.setImageResource(R.drawable.play)
                logo = R.drawable.g_print
            }
            "21" -> {

                scrach_card_name.text = "Amazon-UAE E-Voucher"
                title_head.text = "Amazon-UAE E-Voucher"
                image.setImageResource(R.drawable.amazon)
                logo = R.drawable.amazon
            }
            "22" -> {

                scrach_card_name.text = "Amazon-US E-Voucher"
                title_head.text = "Amazon-US E-Voucher"
                image.setImageResource(R.drawable.amazon)
                logo = R.drawable.amazon
            }
            "23" -> {

                scrach_card_name.text = "NetFlix-UAE E-Voucher"
                title_head.text = "NetFlix-UAE E-Voucher"
                image.setImageResource(R.drawable.netflix)
                logo = R.drawable.netflix_print
            }
            "24" -> {

                scrach_card_name.text = "NetFlix-US E-Voucher"
                title_head.text = "NetFlix-US E-Voucher"
                image.setImageResource(R.drawable.netflix)
                logo = R.drawable.netflix_print
            }
            "25" -> {

                scrach_card_name.text = "Spotify E-Voucher"
                title_head.text = "Spotify E-Voucher"
                image.setImageResource(R.drawable.spotify)
                logo = R.drawable.spotify_print
            }


        }

        confirm.setOnClickListener {
            if (BluetoothUtil().is_bluetooth_enable) {
                confirm.isEnabled = false
                cancel.isEnabled = false
                progress_circular.visibility = View.VISIBLE
                val map: HashMap<String, String?> = hashMapOf(
                    "sourceType" to "Recharge", "planId" to value?.planId.toString(),
                    "quantity" to counts
                )
                val header = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)
                val headerMap = HashMap<String, String?>()
                headerMap["Authorization"] = header
                headerMap["TraceId"] = traceid!!
                viewmodel.saveRecharge(headerMap, map)
            } else {
                enable_bluetooth()
            }


        }

        success_evoucher.setOnClickListener {
            finish()
        }

        another_button.setOnClickListener {
            finish()
        }
        tryevoucher.setOnClickListener {
            progress_circular.visibility = View.VISIBLE
            val header = "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)
            val map: HashMap<String, String?> = hashMapOf(
                "sourceType" to "Recharge", "planId" to value?.planId.toString(),
                "quantity" to counts
            )
            val headerMap = HashMap<String, String?>()
            headerMap["Authorization"] = header
            headerMap["TraceId"] = traceid!!
            viewmodel.saveRecharge(headerMap, map)
        }

        reprint.setOnClickListener {
            for (i in data!!) {
                transId = i.transactionId.toString()
                transCode = i.transactionCode
                rechargeCardNumber_decode1 = decode_base64(i.rechargeCardNumber)!!
                decoded_rechargeCardNumber =
                    decode_base64(rechargeCardNumber_decode1!!)




                if (BluetoothUtil().is_bluetooth_enable()) {
                    logo?.let {
                        logo_print(it)
                    }
                    PrinterFunction(this).call_scratchcaerd_printer_bluetooth(
                        scrach_card_name.text.toString(),
                        "AED",
                        mrp + "",
                        "beneficiaryNumber",
                        decoded_rechargeCardNumber!!,
                        transId,
                        transCode, "Duplicate"
                    )
                } else {
                    enable_bluetooth()
                }


            }

        }


    }

    private fun decode_base64(encoded_data: String): String? {
        val to_decode = encoded_data.toByteArray()
        val decodeValue =
            Base64.decode(to_decode, Base64.DEFAULT)
        Log.d("decoded_Value", String(decodeValue))
        return String(decodeValue)
    }

    private fun enable_bluetooth() {
        val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(bluetoothIntent, BLUETOOTH_ENABLE_REQUEST)

    }

    private fun logo_print(drawable_Resources: Int) {
        try {
            val bmp = BitmapFactory.decodeResource(resources, drawable_Resources)
            if (bmp != null) {
                val command: ByteArray = Utils.decodeBitmap(bmp)
                BluetoothUtil.logoPrint(command)
            } else {

            }
        } catch (e: Exception) {
        }
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
        } else finish()
    }


}
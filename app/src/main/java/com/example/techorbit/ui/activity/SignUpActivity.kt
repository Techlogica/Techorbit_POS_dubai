package com.example.techorbit.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.databinding.ActivitySignUpBinding
import com.example.techorbit.koin.injection.MyViewmodel
import com.example.techorbit.model.error.Error
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.ResultOf
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.ui.fragment.SignupDialogFragment
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.CountryViewmodel
import com.example.techorbit.viewmodel.LoginViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.settings
import kotlinx.android.synthetic.main.signup_dialog.*
import kotlinx.android.synthetic.main.signup_shape.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.RuntimeException
import java.nio.charset.Charset
import javax.inject.Inject


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: LoginViewModel
    var password: String? = null
    var username:String?=null
    var permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE
    )
    val MULTIPLE_PERMISSIONS = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            setPermissions()
        }else{
//            todo here we commended the hardcode device id (line 68 and 104)
//            TechorbitSharedPreference(this).save(KEYS.DEVICEID,DeviceId().serialNumber.toString())
//            TechorbitSharedPreference(this).save(KEYS.DEVICEID,"V26420AN01277")
            TechorbitSharedPreference(this).save(KEYS.DEVICEID,"V222201B02232")
        }


        /**  setting data binding and viewmodel*/
            setUpData()
        observeData()

        settings.setOnClickListener {
            SignupDialogFragment().show(supportFragmentManager, "signupdialog")
        }
        submit.setOnClickListener {
            val deviceid: String? = TechorbitSharedPreference(this).getValue(KEYS.DEVICEID)
            if (deviceid != null) {
//                TechorbitSharedPreference(this).save(KEYS.DEVICEID, deviceid)
                TechorbitSharedPreference(this).save(KEYS.DEVICEID,"V222201B02232")
            } else {
//                TechorbitSharedPreference(this).save(KEYS.DEVICEID, "V26420AN01277")
//                TechorbitSharedPreference(this).save(KEYS.DEVICEID, "Z222201C02247")
                TechorbitSharedPreference(this).save(KEYS.DEVICEID,"V222201B02232")
            }
            validate()

        }

        intent?.let {
            viewModel.username.value = it.getStringExtra("username")
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun setPermissions(){
        if (!Utils.checkPermissions(permissions,this)){
            requestPermissions(permissions,MULTIPLE_PERMISSIONS)
        }else{
            //todo here we commended the hardcode device id (line 68 and 104)
//            TechorbitSharedPreference(this).save(KEYS.DEVICEID, getUniqueIMEIId(this).toString())
//            TechorbitSharedPreference(this).save(KEYS.DEVICEID,"V26420AN01277")
            TechorbitSharedPreference(this).save(KEYS.DEVICEID,"V222201B02232")
        }
    }

    private fun observeData() {
        viewModel.data.observe(this, Observer {
            when (it) {

                is ResultOf.Succes -> {
                    progress_circular.visibility = View.GONE
                    saveData(it)
                }
                is ResultOf.Failiure -> {
                    progress_circular.visibility = View.GONE
                    it.errorBody?.let { errorbody ->
                        val body = errorbody
                        val source = body?.source()
                        val buffer = source?.buffer
                        val responcestring = buffer?.clone()?.readString(Charset.forName("UTF-8"))
                        val gson = Gson().fromJson(responcestring, Error::class.java)
                        showToast(gson.error_message!!)

                    }
                    if (it.isNetworkError) showToast("No internet")

                }
            }
        })
    }

    private fun saveData(it: ResultOf.Succes<SignupData>) {
        TechorbitSharedPreference(this).save(KEYS.TOKEN, it.result.token)
        TechorbitSharedPreference(this).save(KEYS.NAME, it.result.user.name.toString())
        TechorbitSharedPreference(this).save(KEYS.PASS, password.toString())
        TechorbitSharedPreference(this).save(KEYS.USER, username.toString())
        TechorbitSharedPreference(this).save(KEYS.FIRSTNAME, it.result.user.firstName.toString())
        TechorbitSharedPreference(this).save(KEYS.LASTNAME, it.result.user.lastName.toString())
        TechorbitSharedPreference(this).save(KEYS.USERNAME, it.result.user.username.toString())
        TechorbitSharedPreference(this).save(KEYS.DEVICEKEY, it.result.user.deviceKey.toString())
        TechorbitSharedPreference(this).save(
            KEYS.BALANCE,
            it.result.user.wallet?.balance.toString()
        )
        TechorbitSharedPreference(this).save(KEYS.M2MID, it.result.user.m2MId.toString())
        TechorbitSharedPreference(this).save(KEYS.MOBILE, it.result.user.phoneMobile.toString())
        TechorbitSharedPreference(this).save(KEYS.LANDNO, it.result.user.phoneLandline.toString())
        TechorbitSharedPreference(this).save(KEYS.SHOPENAME, it.result.user.shopName.toString())
        TechorbitSharedPreference(this).save(KEYS.TERMINALID, it.result.terminal_id)

        TechorbitSharedPreference(this).save(
            KEYS.DISTRIBUTERCODE,
            it.result.user.distributorCode.toString()
        )
        TechorbitSharedPreference(this).save(
            KEYS.CURRENCY,
            it.result.user.wallet?.currencyAbbreviation.toString()
        )

        //saving menu items

        TechorbitSharedPreference(this).save(KEYS.ETISALATEVOCHER, it.result.menu[1].id.toString())
        TechorbitSharedPreference(this).save(KEYS.ETISALATOTAR, it.result.menu[2].id.toString())
        TechorbitSharedPreference(this).save(KEYS.FIVEEVOCUHER, it.result.menu[3].id.toString())
        TechorbitSharedPreference(this).save(KEYS.HELLOEVOUCHER, it.result.menu[4].id.toString())
        TechorbitSharedPreference(this).save(KEYS.SALIKEVOCUHER, it.result.menu[5].id.toString())
        TechorbitSharedPreference(this).save(KEYS.INTERNATIONAL, it.result.menu[6].id.toString())
        TechorbitSharedPreference(this).save(KEYS.LOCAL_TOPUP, it.result.menu[7].id.toString())
        TechorbitSharedPreference(this).save(
            KEYS.TELIVISIONRECHARGE,
            it.result.menu[8].id.toString()
        )
        TechorbitSharedPreference(this).save(KEYS.ELECTRICITY, it.result.menu[9].id.toString())



        startActivity(Intent(this, HomeActivity::class.java))
        finish()

    }

    private fun validate() {
        when {
            viewModel.username.value.isNullOrBlank() -> {

                showToast("username not empty")
            }
            viewModel.password.value.isNullOrBlank() -> {

                showToast("password not empty")
            }
            else -> {
                hideKeyboard()
                progress_circular.visibility = View.VISIBLE
                username = viewModel.username.value.toString().trim()
                password = viewModel.password.value.toString().trim()
                val deviceId: String? = TechorbitSharedPreference(this).getValue(KEYS.DEVICEID)

                Log.d(TAG, "validate: "+deviceId)
                viewModel.signup(username!!, password!!, deviceId)

            }
        }
    }

    private fun setUpData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        val repositary =
            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
        val factory = ViewModelFactory(repositary)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.loginviewmodel = viewModel
        binding.lifecycleOwner


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            MULTIPLE_PERMISSIONS->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    TechorbitSharedPreference(this).save(KEYS.DEVICEID, getUniqueIMEIId(this).toString())

                }
            }
        }


    }


}
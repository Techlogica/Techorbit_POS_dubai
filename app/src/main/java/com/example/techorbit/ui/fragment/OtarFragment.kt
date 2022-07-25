package com.example.techorbit.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.databinding.ActivitySignUpBinding
import com.example.techorbit.databinding.FragmentOtarBinding
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.ui.activity.ConfirmationActivity
import com.example.techorbit.ui.fragment.live.LiveViewmodel
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.CountryViewmodel
import com.example.techorbit.viewmodel.OtarDuViewmodel
import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_otar.*
import kotlinx.android.synthetic.main.fragment_search_transaction.*
import org.koin.android.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OtarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OtarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var value: String
    private lateinit var binding: FragmentOtarBinding
    private lateinit var viewmodel: OtarDuViewmodel
    private var pending: String = ""
    private val liveViewmodel: LiveViewmodel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otar, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repositary =
            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
        val factory = ViewModelFactory(repositary)
        viewmodel = ViewModelProvider(this, factory).get(OtarDuViewmodel::class.java)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = requireActivity()

        Log.d(TAG, "${TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)}")


        value = arguments?.get("value").toString()
        when (value) {
            "10" -> {
                voucher_imageview.setImageResource(R.drawable.etisalat)
                banner.setImageResource(R.drawable.etisalat)
                voucher_heading.setText("Etisaalat OTAR")
                evoucher_top.setText("Etisalat OTAR    ")
//                doRecharge("etisalat")

            }
            "11" -> {
                voucher_imageview.setImageResource(R.drawable.du)
                banner.setImageResource(R.drawable.du)
                voucher_heading.setText("Du Mpos")
                evoucher_top.setText("Mpos")

            }
        }

        submit.setOnClickListener {
            when (value) {
                "10" -> {
                    doRecharge("etisalat")
                }
                "11" -> {
                    doRecharge("du")
                }
            }
        }

        ButtonFive.setOnClickListener {
            viewmodel.amount.value = "5"
        }
        ButtonTen.setOnClickListener {
            viewmodel.amount.value = "10"
        }
        ButtonTwenty.setOnClickListener {
            viewmodel.amount.value = "20"
        }
        ButtonTwentyFive.setOnClickListener {
            viewmodel.amount.value = "25"
        }

    }

    private fun doRecharge(s: String) {
        val header = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        val type = s

        val amount = viewmodel.amount.value?.toInt()

        when {


            amount == null -> {
                showToast("Enter amount")
            }


            amount < 5 -> {
                showToast("Amount must greater than 5")
            }
            value == "10" && amount % 5 != 0 -> {
                showToast("Amount must be multiple of 5")
            }



            amount > 525 -> showToast("Amount must less than or equal to 525")

            viewmodel.phonenumber.value.isNullOrBlank() -> {
                showToast("Enter your Number")
            }
            isZeroPrefix(viewmodel.phonenumber.value.toString()) -> {
                val phnumber = viewmodel.phonenumber.value.toString()
                if (phnumber.length != 10) {
                    showToast("Must 10 digit")
                }else{
                    getPendingData(s)
                }
            }
            isZeroNotInPrefix(viewmodel.phonenumber.value.toString()) -> {
                val phnumber = viewmodel.phonenumber.value.toString()
                if (phnumber.length != 9) {
                    showToast("Must 9 digit")
                }else {
                    getPendingData(s)
//                    val beneficiaryNumber = viewmodel.phonenumber.value.toString().trim()
//                    val amount = viewmodel.amount.value.toString().trim()
//                    val intent = Intent(requireContext(), ConfirmationActivity::class.java)
////                    intent.putExtra("pending",pending)
//                    intent.putExtra("amount", amount)
//                    intent.putExtra("beneficiaryNumber", beneficiaryNumber)
//                    intent.putExtra("type", s)
//                    startActivity(intent)
                }
            }

//            viewmodel.phonenumber.value!!.length < 9 -> {
//                showToast("Invalid phone number")
//            }

//            viewmodel.phonenumber.value!!.length < 9 -> showToast("Invalid phone number")



            else -> {
//                val beneficiaryNumber = viewmodel.phonenumber.value.toString().trim()
//                val amount = viewmodel.amount.value.toString().trim()
//                val intent = Intent(requireContext(), ConfirmationActivity::class.java)
//                intent.putExtra("amount", amount)
//                intent.putExtra("beneficiaryNumber", beneficiaryNumber)
//                intent.putExtra("type", s)
//                startActivity(intent)
            }
        }


    }

    private fun getPendingData(s: String){
        var mobileNo = ""
        liveViewmodel.dbreportlivedata.observe(requireActivity(), Observer {
            if (it.isNotEmpty()){
                for (i in it!!) {
                    var mobile = ""
                        if (viewmodel.phonenumber.value.toString().length == 9) {
                            mobile = i.beneficiaryNumber.toString().trim()
                        } else {
                            mobile = "0" + i.beneficiaryNumber.toString().trim()
                        }

                        if (viewmodel.phonenumber.value.toString().trim() == mobile) {
                            mobileNo = mobile
                        }
                }
                if (viewmodel.phonenumber.value.toString().trim() == mobileNo.trim()){
                    pending = "Y"
                    goToConfirm(s,pending)
                }else {
                    pending = "N"
                    goToConfirm(s,pending)
                }
            }else {
                pending = "N"
                goToConfirm(s,pending)
            }
        })
    }

    private fun goToConfirm(type: String, pending: String){
        val beneficiaryNumber = viewmodel.phonenumber.value.toString().trim()
        val amount = viewmodel.amount.value.toString().trim()
        val intent = Intent(requireContext(), ConfirmationActivity::class.java)
//                    intent.putExtra("pending",pending)
        intent.putExtra("amount", amount)
        intent.putExtra("beneficiaryNumber", beneficiaryNumber)
        intent.putExtra("type", type)
        intent.putExtra("pending",pending)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OtarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OtarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun checkSelfPermission() {
        var imei = ""
        val tm =
            context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = tm.imei
            }
        } else {
            showToast("please grand permisiions")
            imei = tm.deviceId
        }
        showToast("$imei")

    }

//    private fun getPendingData(){
//        liveViewmodel.dbreportlivedata.observe(requireActivity(), Observer {
//            if (it.isNotEmpty()){
//                for (i in it!!) {
//                    if (viewmodel.phonenumber.value.toString().trim().equals(i.beneficiaryNumber.toString())) {
//                        Log.d("number--",viewmodel.phonenumber.value.toString().trim())
//                        Log.d("benef_number---",i.beneficiaryNumber.toString())
//                        pending = "Y"
//                        confirm.isEnabled = false
//                    }else {
//                        pending = "N"
//                    }
//                }
//            }
//        })
//    }

    override fun onResume() {
        super.onResume()
        activity?.wallet?.text = TechorbitSharedPreference(requireContext()).getValue(KEYS.BALANCE)
    }

}
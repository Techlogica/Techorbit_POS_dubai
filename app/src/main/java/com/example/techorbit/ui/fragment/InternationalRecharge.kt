package com.example.techorbit.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.techorbit.R
import com.example.techorbit.databinding.FragmentInternationalRechargeBinding
import com.example.techorbit.model.prepaidplans.Data
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.ResultOf
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.ui.activity.InternationalRechargeConfirmation
import com.example.techorbit.ui.activity.InternationalRechargePlans
import com.example.techorbit.ui.activity.ProviderListActivity
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.hideKeyboard
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.CountryViewmodel
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_international_recharge.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InternationalRecharge : Fragment() {

    private lateinit var binding: FragmentInternationalRechargeBinding
    private lateinit var viewModel: CountryViewmodel
    private var value: Data? = null
    private var data: com.example.techorbit.model.operator.Data? = null
    private var aed_amount: String? = null
    private var filterUserPrice: String?=null

    private var isMinMax = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_international_recharge,
            container,
            false
        )
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        setUpData()

        observeData()


        binding.click = ClickHandeler()

        val content = SpannableString("Show All Plans").also {
            it.setSpan(UnderlineSpan(), 0, 14, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        show_all_plan.text = content

        show_all_plan.setOnClickListener {
            if (viewModel.phonenumber.value.isNullOrEmpty()) {
                showToast("Enter Phone number")
            } else if (provider.text.isEmpty()) {
                showToast("No Provider")
            } else {
                val intent = Intent(activity, InternationalRechargePlans::class.java)
                intent.putExtra("beneficiaryCountryName", viewModel.countryname.value.toString())
                intent.putExtra("operatorName", provider.text.toString())
                startActivityForResult(intent, 1)
            }
        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            value = data?.getParcelableExtra<Data>("data") as Data
            Log.d("TAG", "onActivityResult: "+value.toString())
            plan.setText(value?.rechargeAmount.toString())
            aed_amount = value?.mrp

            if (value?.maxValue != null && value?.minValue != null) {
                if (value?.minValue!=value?.maxValue){
                    usr_value_layout.visibility = View.VISIBLE
                    cal_culatemount.visibility = View.VISIBLE
                    plan.setText(value?.benefits.toString())
                    isMinMax = true
//                    viewModel.usrValue.value=""
                    viewModel.usrValue.value=value?.mrp
                }else isMinMax=false

            } else {
                isMinMax = false
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("name")
            provider.setText(name)


        }
    }

    private fun observeData() {
        viewModel.prepaidLiveData.observe(requireActivity(), Observer {
            when (it) {
                is ResultOf.Succes -> {
                    progress_circular.visibility = View.GONE
                    provider.setText(it.result.operatorName)
                    select_provider.visibility = View.VISIBLE
//                    chageprovider.visibility = View.VISIBLE
                }
                is ResultOf.Failiure -> {
                    showToast("Failed to load  Service Provider")
                }
            }
        })


    }


    inner class ClickHandeler {
        fun selectCountry(view: View) {
            view.findNavController().navigateUp()
        }

        fun click(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            data = parent?.selectedItem as com.example.techorbit.model.operator.Data
//            Toast.makeText(view?.context, "${data!!.name}", Toast.LENGTH_SHORT).show()
        }

        fun submit(view: View) {

            when {
                viewModel.phonenumber.value.isNullOrBlank() -> {
                    showToast("Phone number not empty")
                }
                plan.text.isNullOrEmpty() -> {
                    showToast("Enter a valid plan")
                }
                isMinMax->{

                    showToast("Enter Your Amount")

                }

                !isMinMax->{
                    if (value?.maxValue != null && value?.minValue != null){
                        val calculatevalue=filterUserPrice?.toDouble();
                        val minvalue=value?.minValue?.toDouble()
                        val maxValue=value?.maxValue?.toDouble()

                        calculatevalue?.let {
                            if (it >= minvalue!!&& it<=maxValue!!){
                               showRecharageACtivity(filterUserPrice)
                            }else showToast("Wrong Amount")
                        }
                    }else{
                        showRecharageACtivity(filterUserPrice)
                    }
                }

            }
        }


    }

    private fun showRecharageACtivity(filterUserPrice: String?) {
        val deviceKey = TechorbitSharedPreference(requireContext()).getValue(KEYS.DEVICEKEY)
        val deviceId = TechorbitSharedPreference(requireContext()).getValue(KEYS.DEVICEID)

        val intent = Intent(requireContext(), InternationalRechargeConfirmation::class.java)
        intent.putExtra(
            "beneficiaryNumber",
            viewModel.phonenumber.value.toString().trim()
        )
        intent.putExtra("denominationId", value?.denominationId)
        intent.putExtra("operatorName", provider.text.toString())
        intent.putExtra("deviceKey", deviceKey)
        intent.putExtra("deviceId", deviceId)
        intent.putExtra("mrp", value?.mrp)
        intent.putExtra("rechargeAmount", value?.rechargeAmount)
        intent.putExtra("localCurrency", value?.localCurrency)
        intent.putExtra("extension", arguments?.get("abrevation").toString())
        intent.putExtra(
            "beneficiaryCountryName",
            viewModel.countryname.value.toString()
        )
        intent.putExtra("filteruserPrice",filterUserPrice)
        intent.putExtra("useramount",viewModel.uservalue_liveData.value.toString())
        requireContext().startActivity(intent)


    }

    private fun setUpData() {

        val repositary =
            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
        val factory = ViewModelFactory(repositary)
        viewModel = ViewModelProvider(this, factory).get(CountryViewmodel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner

        val id = arguments?.get("id")
        val abrevation = arguments?.get("abrevation")
        val numberlength = arguments?.get("numberLength")
        val name = arguments?.get("name")
        viewModel.countryname.value = name.toString()
        viewModel.abrevation.value = "+${abrevation.toString()}"
        val header =
            "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)

        binding.provider.setOnClickListener {
            val intent = Intent(requireContext(), ProviderListActivity::class.java)
            intent.putExtra("id", "$id")
            startActivityForResult(intent, 2)
        }




        viewModel.phonelivedata.observe(requireActivity(), Observer { it ->

            if (it.length.toString() == numberlength) {


                val hashMap = HashMap<String, String>().apply {
                    put("beneficiaryCountryId", id.toString())
                    put("beneficiaryNumber", it)
                    put(
                        "deviceId",
                        TechorbitSharedPreference(requireContext()).getValue(KEYS.DEVICEID)
                            .toString()
                    )
                }
                hideKeyboard()
                progress_circular.visibility = View.VISIBLE


                viewModel.getPrepaidOperator(header!!, hashMap)


            }

        })

        viewModel.uservalue_liveData.observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
//                var mrp = value?.mrp?.toDouble()
//                val rechargeAmount = value?.rechargeAmount?.toDouble()
//                val user_value = it.toDouble()
//
//                val calculation:Double? = (rechargeAmount?.div(mrp!!)?.times(user_value))
//                val commission=value?.commisionPer
//
//                Log.d(
//                    "TAG",
//                    "setUpData:commission:$commission mrp==$mrp,rechargeAmount==$rechargeAmount,Enter Amount===$user_value, Calculation==$calculation"
//                )
//
//
//                filterUserPrice= "%.2f".format(calculation)
                val header =
                    "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
                val hashMap=HashMap<String,String?>()
                hashMap["sendValue"]=it
                hashMap["denominationId"] = value?.denominationId.toString()

                viewModel.check(header,it,value?.denominationId.toString())
//                viewModel.calculateValue.value = "${value?.localCurrency}: $filterUserPrice"
                isMinMax=false


            } else {
                viewModel.calculateValue.value = "0"
                isMinMax=true

            }
        })

        viewModel.mrpLiveData.observe(requireActivity(), Observer {
            when(it){
                is ResultOf.Succes->{
                    filterUserPrice=it.result.recharge_amount
                    viewModel.calculateValue.value="${value?.localCurrency}: ${it.result.recharge_amount}"
                }

            }
        })

    }

    override fun onResume() {
        super.onResume()
        activity?.wallet?.text = TechorbitSharedPreference(requireContext()).getValue(KEYS.BALANCE)
    }


}
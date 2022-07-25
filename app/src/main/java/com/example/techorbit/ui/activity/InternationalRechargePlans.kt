package com.example.techorbit.ui.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.model.prepaidplans.Data
import com.example.techorbit.repositary.InternationalRechargeFactory
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.ResultOf
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.ui.fragment.InternationalRecharge
import com.example.techorbit.ui.fragment.datamb.DataMbFragment
import com.example.techorbit.ui.fragment.special.SpecialFragment
import com.example.techorbit.ui.fragment.topup.TopupFragment
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.OtarDuViewmodel
import com.example.techorbit.viewmodel.RechargePlanViewModel
import kotlinx.android.synthetic.main.activity_international_recharge_plans.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InternationalRechargePlans : AppCompatActivity() {

    private lateinit var rechargePlanViewModel: RechargePlanViewModel
    private var list = ArrayList<Data>()
    private lateinit var progress: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_international_recharge_plans)
        progress = ProgressDialog(this)
        progress.setCanceledOnTouchOutside(false)

        setData()

        val calender1 = Calendar.getInstance()
        val date = calender1.time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatdate = sdf.format(date)

        val sharedDate = TechorbitSharedPreference(this).getValue(KEYS.DATEI)



        when {
            sharedDate == null -> {
                rechargePlanViewModel.clearAll()
                TechorbitSharedPreference(this).save(KEYS.DATEI, formatdate)
            }
            sharedDate.equals(formatdate) -> {

            }
            else -> {
                rechargePlanViewModel.clearAll()
                TechorbitSharedPreference(this).save(KEYS.DATEI, formatdate)
            }
        }

        refresh.setOnClickListener {

            if (list.isNotEmpty()) {
                rechargePlanViewModel.clearAll()
            } else {
                val header = "Bearer ${TechorbitSharedPreference(this).getValue(KEYS.TOKEN)}"

                /** call api when local db has no data*/

                rechargePlanViewModel.getPrepaidplans(header)
                progress.show()
            }
        }

        observeData()


    }

    private fun observeData() {
        /** data from server*/
        rechargePlanViewModel.liveData.observe(this, Observer {
            when (it) {
                is ResultOf.Succes -> {
                    progress.dismiss()
                    internationalRecharge.visibility = View.VISIBLE
                    list = it.result.data as ArrayList<Data>
//                    list.sortBy {
//                        it.mrp
//                    }
                    /** insert data to localdb */
                    rechargePlanViewModel.saveData(list)
                }

                is ResultOf.Failiure -> {
                    progress.dismiss()
                    showToast("Something went wrong,Try again")
                }
            }
        })


        /** data from local DB*/

        rechargePlanViewModel.prepaidLiveData.observe(this, Observer {
            if (it.isEmpty()) {
                val header = "Bearer ${TechorbitSharedPreference(this).getValue(KEYS.TOKEN)}"

                /** call api when local db has no data*/

                rechargePlanViewModel.getPrepaidplans(header)
                progress.show()
            } else {
                list.clear()
                list = it as ArrayList<Data>

                Log.d("TAG", "has data: ")
                setFragmemt(DataMbFragment(), list)
            }
        })


        rechargePlanViewModel.data.observe(this, Observer {
            it?.let {
                val intent = Intent(this, InternationalRecharge::class.java)
                intent.putExtra("data", it)
                setResult(Activity.RESULT_OK, intent)
                finish()

                Log.d("TAG", "observeData: $it")
            }
        })

    }

    private fun setData() {
        val repositary =
            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
        val factory = InternationalRechargeFactory(repositary, this)
        rechargePlanViewModel =
            ViewModelProvider(this, factory).get(RechargePlanViewModel::class.java)

        back.setOnClickListener { finish() }
        setFragmemt(DataMbFragment(), list)


        button_first.setOnClickListener {

            setFragmemt(DataMbFragment(), list)
            button_first.setBackgroundColor(resources.getColor(R.color.white))
            button_second.setBackgroundColor(resources.getColor(R.color.dark_grey))
            button_third.setBackgroundColor(resources.getColor(R.color.dark_grey))
        }

        button_second.setOnClickListener {

            setFragmemt(TopupFragment(), list)
            button_first.setBackgroundColor(resources.getColor(R.color.dark_grey))
            button_second.setBackgroundColor(resources.getColor(R.color.white))
            button_third.setBackgroundColor(resources.getColor(R.color.dark_grey))
        }

        button_third.setOnClickListener {

            setFragmemt(SpecialFragment(), list)
            button_first.setBackgroundColor(resources.getColor(R.color.dark_grey))
            button_second.setBackgroundColor(resources.getColor(R.color.dark_grey))
            button_third.setBackgroundColor(resources.getColor(R.color.white))
        }


    }

    private fun setFragmemt(fragment: Fragment, prepaidPlans: ArrayList<Data>) {
        val intent = intent
        val beneficiaryCountryName = intent.getStringExtra("beneficiaryCountryName")
        val operatorName = intent.getStringExtra("operatorName")
        val bundle = Bundle()
        bundle.putParcelableArrayList("value", prepaidPlans)
        bundle.putString("beneficiaryCountryName", beneficiaryCountryName)
        bundle.putString("operatorName", operatorName)
        fragment.arguments = bundle
        val tranasaction = supportFragmentManager.beginTransaction()
        tranasaction.replace(R.id.framlayout, fragment)
        tranasaction.commit()


    }
}
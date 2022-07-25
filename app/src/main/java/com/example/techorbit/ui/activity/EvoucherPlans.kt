package com.example.techorbit.ui.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.model.Plans
import com.example.techorbit.model.five.Recharge
import com.example.techorbit.ui.fragment.live.LiveFragment
import com.example.techorbit.ui.fragment.live.LiveViewmodel
import com.example.techorbit.viewmodel.EvoucherPlanViewmodel
import kotlinx.android.synthetic.main.activity_evoucher_confirmation.*
import kotlinx.android.synthetic.main.activity_evoucher_plans.*
import org.koin.android.viewmodel.ext.android.viewModel

class EvoucherPlans : AppCompatActivity() {

    private lateinit var viewmodel: EvoucherPlanViewmodel
    private val liveViewmodel: LiveViewmodel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evoucher_plans)
        viewmodel = ViewModelProvider(this).get(EvoucherPlanViewmodel::class.java)

        viewmodel.data.observe(this, Observer {

            val plans = Plans()
            plans.benefits = it.benefits
            plans.downlodedCount = it.downlodedCount
            plans.localAmount = it.localAmount
            plans.localCurrency = it.localCurrency
            plans.mrp = it.mrp
            plans.planId = it.planId


            Log.d("TAG", "data $it")
            it?.let {
                val intent = intent
                intent.putExtra("key", plans)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
        back.setOnClickListener {
            finish()
        }

        refresh.setOnClickListener {
            liveViewmodel.clearAll()
        }


        setFragmemt(LiveFragment())
        setLogo()
    }

    private fun setFragmemt(fragment: Fragment) {
        val value: String = intent.getStringExtra("value").toString()
        val bundle = Bundle()
        bundle.putString("value", value)

        fragment.arguments = bundle
        val tranasaction = supportFragmentManager.beginTransaction()
        tranasaction.replace(R.id.framlayout, fragment)
        tranasaction.commit()

    }

    private fun setLogo() {
        when (intent.getStringExtra("value").toString()) {
            "0" -> {

                logo.setImageResource(R.drawable.du)

            }

            "1" -> {

                logo.setImageResource(R.drawable.etisalat)

            }
            "2" -> {
                logo.setImageResource(R.drawable.five)
            }
            "3" -> {

                logo.setImageResource(R.drawable.hello)
            }
            "4" -> {
                logo.setImageResource(R.drawable.salik)
            }
            "5" -> {
                logo.setImageResource(R.drawable.ic_play_station)
            }

            "6" -> {

                logo.setImageResource(R.drawable.virgin_mobiles)
            }
            "7" -> {

                logo.setImageResource(R.drawable.xbox)
            }
            "8" -> {


                logo.setImageResource(R.drawable.itunes)
            }
            "9" -> {


                logo.setImageResource(R.drawable.pubg)
            }
            "12" -> {


                logo.setImageResource(R.drawable.play)
            }
            "14" -> {

                logo.setImageResource(R.drawable.freefire)

            }
            "15" -> {

                logo.setImageResource(R.drawable.xbox)
            }
            "16" -> {
                logo.setImageResource(R.drawable.ic_play_station)
            }
            "17" -> {

                logo.setImageResource(R.drawable.ic_play_station)
            }

            "18" -> {

                logo.setImageResource(R.drawable.ic_play_station)
            }


            "19" -> {


                logo.setImageResource(R.drawable.itunes)
            }

            "20" -> {
                logo.setImageResource(R.drawable.play)
            }
            "21" -> {

                logo.setImageResource(R.drawable.amazon)
            }
            "22" -> {

                logo.setImageResource(R.drawable.amazon)
            }
            "23" -> {

                logo.setImageResource(R.drawable.netflix)
            }
            "24" -> {

                logo.setImageResource(R.drawable.netflix)
            }
            "25" -> {
                logo.setImageResource(R.drawable.spotify)
            }

        }

    }
}

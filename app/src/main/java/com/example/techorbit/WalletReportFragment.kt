package com.example.techorbit

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.example.techorbit.adapter.WalletReportAdapter
import com.example.techorbit.databinding.FragmentWalletReportBinding
import com.example.techorbit.model.reports.walletreports.ModelWallet
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.HomeViewmodel
import com.example.techorbit.viewmodel.WalletReport
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_wallet_report.*
import kotlinx.android.synthetic.main.wallet_credit_outstanding.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class WalletReportFragment : Fragment() {

    private val viewmodel: WalletReport by viewModel()
    var start: String? = null
    var end: String? = null
    private lateinit var mCalender: Calendar
    private lateinit var binding: FragmentWalletReportBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_wallet_report, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCalender = Calendar.getInstance()

//
        getWallet()
        observeData()
//
        val monthString =
            DateFormat.format("MMM", Calendar.getInstance().timeInMillis) as String // Jun

        val day =
            DateFormat.format("dd", Calendar.getInstance().timeInMillis) as String // 20

        val year =
            DateFormat.format("yyyy", Calendar.getInstance().timeInMillis) as String // 2013

        setupData("$day $monthString $year")
//
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

        binding.searchReport.setOnClickListener {
            showToast("Loading...")
            binding.progressCircular.visibility = View.VISIBLE
            showData(start, end)
        }


    }

    private fun getWallet() {
        val header = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viewmodel.getWallet(header!!)
        viewmodel.walletLiveData.observe(requireActivity(), androidx.lifecycle.Observer {
            when (it) {
                is ResultOf.Succes -> {
                    wallet_amount.visibility = View.VISIBLE
                    wallet_balance.text = "AED ${it.result.outstandingCredit}"
                    balance.text = "AED ${it.result.balance}"
                    opening_balance.text =
                        "Today's Opening Balance: AED ${it.result.openingBalance}"
                    TechorbitSharedPreference(requireContext()).save(
                        KEYS.BALANCE,
                        it.result.balance
                    )
                    requireActivity().wallet.text =
                        TechorbitSharedPreference(requireContext()).getValue(KEYS.BALANCE)
                }
                is ResultOf.Failiure -> {
                }
            }
        })
    }

    private fun showData(start: String?, end: String?) {

        Log.d("TAG", "$start:......$end ")
        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viewmodel.getReports(head, start!!, end!!)
//        viewmodel.liveData.observe(requireActivity(), androidx.lifecycle.Observer {
//            when (it) {
//                is ResultOf.Succes -> {
////                    binding.progressCircular.visibility=View.INVISIBLE
//                    setRecyclerview(it)
//                }
//                is ResultOf.Failiure -> {
////                    binding.progressCircular.visibility=View.INVISIBLE
//                    showToast("something went wrong")
//                }
//            }
//        })

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
        binding.progressCircular.visibility = View.VISIBLE
        calender_start_text.text = "$s"
        calender_end_text.text = "$s"
        val hour = Calendar.getInstance().get(Calendar.HOUR)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        val second = Calendar.getInstance().get(Calendar.SECOND)

        start = "$s 00:00:00 GMT"
        end = "$s 23:59:59 GMT"


        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viewmodel.getReports(head, start!!, end!!)

    }

    private fun setRecyclerview(it: ResultOf.Succes<ModelWallet>) {


        if (it.result.data.isNotEmpty()) {
            val adapter = WalletReportAdapter()
            recycler_view.adapter = adapter
            adapter.bindData(it.result.data)
        } else {
            showToast("No data")
        }
    }

    private fun observeData() {
        viewmodel.liveData.observe(requireActivity(), androidx.lifecycle.Observer {

            when (it) {
                is ResultOf.Succes -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    setRecyclerview(it)
                }
                is ResultOf.Failiure -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    showToast("something went wrong")
                }
            }
        })
    }


}
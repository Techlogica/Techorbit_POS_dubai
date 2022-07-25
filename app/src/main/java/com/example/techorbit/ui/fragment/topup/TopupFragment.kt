package com.example.techorbit.ui.fragment.topup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.adapter.RechargeAdapter
import com.example.techorbit.model.prepaidplans.Data
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.RechargePlanViewModel
import kotlinx.android.synthetic.main.fragment_topup.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.ArrayList
import kotlin.math.log


class TopupFragment : Fragment() {

    private var datalist = ArrayList<Data>()
    private lateinit var rechargePlanViewModel: RechargePlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repositary =
            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
        val factory = ViewModelFactory(repositary)
        rechargePlanViewModel =
            ViewModelProvider(requireActivity(), factory).get(RechargePlanViewModel::class.java)


        val bundle = arguments
        val data: ArrayList<Data>? = bundle?.getParcelableArrayList("value")
        val beneficiaryCountryName = bundle?.getString("beneficiaryCountryName")
        val operatorName = bundle?.getString("operatorName")


        for (i in data!!) {

            i.let {
                if (it.rechargeSubType == "TOPUP" && it.beneficiaryCountryName == beneficiaryCountryName
                    && it.operatorName == operatorName
                ) {
                    datalist.add(it)
                }
            }
        }

        datalist.sortBy { it.rechargeAmount?.toDouble() }

        for (i in datalist) {
            Log.d("TAG", "onViewCreated: $i")
        }
        val adapter = RechargeAdapter { selecteditem: Data -> clickHandler(selecteditem) }
        adapter.bindData(datalist)
        recycler_view.adapter = adapter
        bundle.clear()
    }

    private fun clickHandler(data: Data) {
//        showToast(data.mrp.toString())

        rechargePlanViewModel.setData(data)

    }

    companion object {
    }
}
package com.example.techorbit.ui.fragment.datamb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.adapter.EvocherAdapter
import com.example.techorbit.adapter.RechargeAdapter
import com.example.techorbit.model.prepaidplans.Data
import com.example.techorbit.repositary.InternationalRechargeFactory
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.EvoucherPlanViewmodel
import com.example.techorbit.viewmodel.RechargePlanViewModel
import kotlinx.android.synthetic.main.fragment_data_mb.*
import kotlinx.android.synthetic.main.fragment_data_mb.recycler_view
import kotlinx.android.synthetic.main.fragment_topup.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DataMbFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataMbFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var datalist = ArrayList<Data>()
    private  lateinit var rechargePlanViewModel: RechargePlanViewModel
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
        return inflater.inflate(R.layout.fragment_data_mb, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repositary =
            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
        val factory = InternationalRechargeFactory(repositary,requireActivity())
        rechargePlanViewModel =
            ViewModelProvider(requireActivity(), factory).get(RechargePlanViewModel::class.java)


        val bundle = arguments
        val data: ArrayList<Data>? = bundle?.getParcelableArrayList("value")
        val beneficiaryCountryName = bundle?.getString("beneficiaryCountryName")
        val operatorName = bundle?.getString("operatorName")


        for (i in data!!) {
            i?.let {
                if (it.rechargeSubType == "dataMB" && it.beneficiaryCountryName == beneficiaryCountryName && it.operatorName == operatorName) {
                    datalist.add(it)
                }
            }
        }

        datalist.sortBy { it.rechargeAmount?.toDouble() }
        val adapter = RechargeAdapter { selecteditem: Data -> clickHandler(selecteditem) }
        adapter.bindData(datalist)
        recycler_view.adapter = adapter

        bundle.clear()


    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DataMbFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun clickHandler(data: Data) {

        rechargePlanViewModel.setData(data)
    }
}
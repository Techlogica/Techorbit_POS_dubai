package com.example.techorbit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.techorbit.R
import com.example.techorbit.adapter.CommissionAdapter
import com.example.techorbit.model.me.CommissionRate
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.CommissionViewmodel
import kotlinx.android.synthetic.main.fragment_commision.*
import org.koin.android.viewmodel.ext.android.viewModel


class CommisionFragment : Fragment() {

    private val viewmodel: CommissionViewmodel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commision, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val header = "Bearer ${TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)}"
        viewmodel.getcommisionRate(header)

        viewmodel.liveData.observe(requireActivity(), Observer {
            when (it) {
                is ResultOf.Succes -> {
                    val data = it.result.user.commissionRates
                    setAdapter(data)
                }
                is ResultOf.Failiure -> {
                    showToast("Failed")
                }
            }
        })
    }

    private fun setAdapter(data: List<CommissionRate>) {
        val adapter = CommissionAdapter()
        recycler_view.adapter = adapter
        adapter.bind(data)
    }


}
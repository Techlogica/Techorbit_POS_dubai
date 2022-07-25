package com.example.techorbit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.techorbit.R
import com.example.techorbit.adapter.NotificationAdapter
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.NotificationViewmodel
import kotlinx.android.synthetic.main.fragment_notification.*
import org.koin.android.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {

    private val viewmodel:NotificationViewmodel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()

        observeData()
    }

    private fun observeData() {
        viewmodel.liveData.observe(requireActivity(), Observer {
            progress_circular.visibility=View.GONE
            when(it){
                is ResultOf.Succes -> {
                    if (it.result.data.isNotEmpty()){
                        val adapter=NotificationAdapter()
                        recycler_view.adapter=adapter
                        adapter.bindData(it.result.data)
                    }else{
                        showToast("No Notifications")
                    }
                }

                is ResultOf.Failiure -> {
                   showToast("${it.message}")
                }
            }
        })
    }

    private fun setData() {

        progress_circular.visibility=View.VISIBLE
        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viewmodel.getNotifications(head)

    }

}
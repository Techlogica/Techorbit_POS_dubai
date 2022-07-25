package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.reciept.RecieptReport
import com.example.techorbit.model.reports.vendor.ModelVendror
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class RecieptViewmodel(val repositary: TechorbitRepositary) : ViewModel() {

    private val mutableLiveData = MutableLiveData<ResultOf<RecieptReport>>()
    val liveData: LiveData<ResultOf<RecieptReport>> = mutableLiveData
    fun getVendorReports(head: String, start: String, end: String) = viewModelScope.launch {
        mutableLiveData.value = repositary.getRecieptReport(head, start, end)
    }
}
package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.reports.terminal.ModelTerminalRepors
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class TerminalSalesReports(val repositary: TechorbitRepositary):ViewModel() {

    private val mutableLiveData= MutableLiveData<ResultOf<ModelTerminalRepors>>()
    val liveData : LiveData<ResultOf<ModelTerminalRepors>> =mutableLiveData
    fun getSalesReports(head:String,start:String,end:String)=viewModelScope.launch {
        mutableLiveData.value = repositary.getSalesReports(head,start,end)
    }


    private val recharegemutableLiveData = MutableLiveData<ResultOf<Any>>()
    val rechargeliveData: LiveData<ResultOf<Any>> = recharegemutableLiveData
    fun getTransactionReports(head: String, start: String, end: String) = viewModelScope.launch {
//        mutableLiveData.value = repositary.getTransaction(head, start, end)
        recharegemutableLiveData.value = repositary.getTransactioReport(head, start, end)
    }
}
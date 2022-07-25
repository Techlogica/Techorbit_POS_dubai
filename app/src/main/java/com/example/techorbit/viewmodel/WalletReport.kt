package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.reports.walletreports.ModelWallet
import com.example.techorbit.model.wallet.MyWallet
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class WalletReport(val techorbitRepositary: TechorbitRepositary):ViewModel() {

    private val walletMutableLiveData = MutableLiveData<ResultOf<MyWallet>>()
    val walletLiveData: LiveData<ResultOf<MyWallet>> = walletMutableLiveData

    fun getWallet(header: String) = viewModelScope.launch {
        walletMutableLiveData.value = techorbitRepositary.getWallet(header)
    }

    private val mutableLiveData= MutableLiveData<ResultOf<ModelWallet>>()
    val liveData :LiveData<ResultOf<ModelWallet>> =mutableLiveData
    fun getReports(head:String,start:String,end:String)=viewModelScope.launch {
        mutableLiveData.value = techorbitRepositary.getWalletReports(head,start,end)
    }




}
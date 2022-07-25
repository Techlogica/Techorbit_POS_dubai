package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.TransactionModel
import com.example.techorbit.model.purchase.ModelPurchase
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class PurchaseViewmodel(val repositary: TechorbitRepositary):ViewModel() {
    private val mutableLiveData = MutableLiveData<ResultOf<ModelPurchase>>()
    val liveData: LiveData<ResultOf<ModelPurchase>> = mutableLiveData
    fun getPurchaseReport(head: String, start: String, end: String) = viewModelScope.launch {
        mutableLiveData.value = repositary.getPurchase(head, start, end)
    }
}

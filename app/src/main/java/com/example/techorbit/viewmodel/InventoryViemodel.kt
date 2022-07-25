package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.reports.inventory.ModelInventory
import com.example.techorbit.model.reports.vendor.ModelVendror
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch
import org.json.JSONObject

class InventoryViemodel(val repositary: TechorbitRepositary):ViewModel() {
    private val mutableLiveData= MutableLiveData<ResultOf<Any>>()
    val liveData : LiveData<ResultOf<Any>> =mutableLiveData
    fun getInventory(head:String)=viewModelScope.launch {

        mutableLiveData.value=repositary.getInventoryReports(head)


    }
}
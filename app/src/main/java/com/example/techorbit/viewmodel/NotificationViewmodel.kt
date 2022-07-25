package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.notification.ModelNotifications
import com.example.techorbit.model.reports.vendor.ModelVendror
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class NotificationViewmodel(private val repositary: TechorbitRepositary):ViewModel() {

    private val mutableLiveData= MutableLiveData<ResultOf<ModelNotifications>>()
    val liveData : LiveData<ResultOf<ModelNotifications>> =mutableLiveData
    fun getNotifications(head:String)=viewModelScope.launch {
        mutableLiveData.value = repositary.getNotifications(head)
    }
}
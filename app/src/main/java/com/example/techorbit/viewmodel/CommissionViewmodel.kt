package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.me.CommissionRate
import com.example.techorbit.model.me.ModelCommission
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class CommissionViewmodel(val repositary: TechorbitRepositary) : ViewModel() {

    private val mutableLiveData = MutableLiveData<ResultOf<ModelCommission>>()
    val liveData: LiveData<ResultOf<ModelCommission>> = mutableLiveData

    fun getcommisionRate(header: String) = viewModelScope.launch {
        mutableLiveData.value = repositary.getCommissionRate(header)

    }

}
package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.techorbit.model.five.Recharge

class EvoucherPlanViewmodel : ViewModel() {
    private val _setdata = MutableLiveData<Recharge>()
    val data: LiveData<Recharge> = _setdata

    fun setData(data: Recharge) {
        _setdata.value = data
    }




}
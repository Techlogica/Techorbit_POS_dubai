package com.example.techorbit.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.db.TechorbitDb
import com.example.techorbit.model.prepaidplans.Data
import com.example.techorbit.model.prepaidplans.PrepaidPlans
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class RechargePlanViewModel(val repositary: TechorbitRepositary, val context: Context) :
    ViewModel() {

    private val mutableLiveData = MutableLiveData<ResultOf<PrepaidPlans>>()
    val liveData: LiveData<ResultOf<PrepaidPlans>> = mutableLiveData
    fun getPrepaidplans(header: String) = viewModelScope.launch {
        mutableLiveData.value = repositary.getPrepaidPlans(header)
    }

    private val _data = MutableLiveData<Data>()
    val data: LiveData<Data> = _data
    fun setData(data: Data) {
        _data.value = data
    }

    val techorbitDb = TechorbitDb.getInstance(context)
    val prepaidLiveData= techorbitDb.datadao().getAllData()
    fun saveData(list: List<Data>) = viewModelScope.launch {
        techorbitDb.datadao().insertData(list)
    }
    fun clearAll()=viewModelScope.launch{
        techorbitDb.datadao().clearAll()
    }

}
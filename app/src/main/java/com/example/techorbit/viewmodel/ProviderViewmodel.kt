package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.operator.Operators
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class ProviderViewmodel(val repositary: TechorbitRepositary) : ViewModel() {

    private val _operatorList = MutableLiveData<ResultOf<Operators>>()
    val operatorList: LiveData<ResultOf<Operators>> = _operatorList


    fun getPrepaidOperatorList(header: String, beneficiaryId: String) = viewModelScope.launch {
        _operatorList.value = repositary.getPrepaidoperatorList(header, beneficiaryId)
    }
}
package com.example.techorbit.viewmodel

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.country.CountryList
import com.example.techorbit.model.operator.Operators
import com.example.techorbit.model.prepaidoperator.PrepaidOperator
import com.example.techorbit.model.prepaidplans.PrepaidPlans
import com.example.techorbit.model.traceid.ModeTraceId
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class CountryViewmodel(val repositary: TechorbitRepositary) : ViewModel(), Observable {

    private val mutableLiveData = MutableLiveData<ResultOf<CountryList>>()
    val liveData: LiveData<ResultOf<CountryList>> = mutableLiveData
    fun getCountries(header: String, map: HashMap<String, String>) = viewModelScope.launch {
        mutableLiveData.value = repositary.getCountries(header, map)
    }

    val country = MutableLiveData<String>()

    init {

        country.value = "India"
    }

    @Bindable
    val countryname = MutableLiveData<String>()

    @Bindable
    val abrevation = MutableLiveData<String>()

    @Bindable
    val phonenumber = MutableLiveData<String>()

    @Bindable
    val operator = MutableLiveData<String>()


    @Bindable
    val usrValue=MutableLiveData<String>()

    @Bindable
    val calculateValue=MutableLiveData<String>()

    val phonelivedata: LiveData<String> = phonenumber

    val uservalue_liveData:LiveData<String> =usrValue;

    private val prepaidMutableLiveData = MutableLiveData<ResultOf<PrepaidOperator>>()
    val prepaidLiveData: LiveData<ResultOf<PrepaidOperator>> = prepaidMutableLiveData

    fun getPrepaidOperator(header: String, map: HashMap<String, String>) = viewModelScope.launch {
        prepaidMutableLiveData.value = repositary.getPrepaidOperator(header, map)
    }

    private val _operatorList = MutableLiveData<ResultOf<Operators>>()
    val operatorList: LiveData<ResultOf<Operators>> = _operatorList


    fun getPrepaidOperatorList(header: String, beneficiaryId: String) = viewModelScope.launch {
       _operatorList.value = repositary.getPrepaidoperatorList(header, beneficiaryId)
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun check(header: String,sendvalue:String,denominationId:String ){
       viewModelScope.launch {
           mrpMutableLiveData.value=repositary.checkMrp(header, sendvalue,denominationId)
       }
    }

    private val mrpMutableLiveData=MutableLiveData<ResultOf<ModeTraceId>>()
    val mrpLiveData:LiveData<ResultOf<ModeTraceId>> = mrpMutableLiveData;

}
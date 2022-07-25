package com.example.techorbit.viewmodel

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.otarrecharge.DoOtarRecharge
import com.example.techorbit.model.retriverecharge.RetriveRecharge
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.model.wallet.MyWallet
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class OtarDuViewmodel(val repositary: TechorbitRepositary) : ViewModel(), Observable {

    @Bindable
    var amount = MutableLiveData<String>()

    @Bindable
    val phonenumber = MutableLiveData<String>()

    private val mutableLiveData = MutableLiveData<ResultOf<DoOtarRecharge>>()
    val liveData: LiveData<ResultOf<DoOtarRecharge>> = mutableLiveData




    fun saveRecharge(header: HashMap<String,String>, map: HashMap<String, String>) = viewModelScope.launch {
        mutableLiveData.value = repositary.doRecharge(header, map)
    }

    private val rechargeStatusMutableLiveData=MutableLiveData<ResultOf<RetriveRecharge>>()
    val rechargeStatusLiveData= rechargeStatusMutableLiveData
    fun checkRechargeStatus(header: String,map: HashMap<String, String?>)=viewModelScope.launch {
        rechargeStatusLiveData.value=repositary.checkRechargeStatus(header,map)
    }


    private val walletMutablelivedata = MutableLiveData<ResultOf<MyWallet>>()
    val walletlivedata: LiveData<ResultOf<MyWallet>> = walletMutablelivedata

    fun getWallet(header: String) = viewModelScope.launch {
        walletMutablelivedata.value = repositary.getWallet(header)
    }


    private val _data = MutableLiveData<ResultOf<SignupData>>()
    val data: LiveData<ResultOf<SignupData>> = _data

    fun signup(username: String, password: String, deviceId: String?) = viewModelScope.launch {

        _data.value = repositary.signUp(username, password, deviceId)
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}
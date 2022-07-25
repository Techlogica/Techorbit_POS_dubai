package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.international.InternationalRechargeModel
import com.example.techorbit.model.retriverecharge.RetriveRecharge
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.model.wallet.MyWallet
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class InternationalRechargeViewmodel(val repositary: TechorbitRepositary) : ViewModel() {

    private val _mutablelivedata = MutableLiveData<ResultOf<InternationalRechargeModel>>()
    val liveData: LiveData<ResultOf<InternationalRechargeModel>> = _mutablelivedata
    fun interNationalRecharge(header: HashMap<String,String>, hashMap: HashMap<String,String>) =
        viewModelScope.launch {
            _mutablelivedata.value = repositary.internationalRecharge(header,hashMap)
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

    private val rechargeStatusMutableLiveData=MutableLiveData<ResultOf<RetriveRecharge>>()
    val rechargeStatusLiveData= rechargeStatusMutableLiveData

    fun checkRechargeStatus(header: String,map: HashMap<String, String?>)=viewModelScope.launch {
        rechargeStatusLiveData.value=repositary.checkRechargeStatus(header,map)
    }




}
package com.example.techorbit.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.model.wallet.MyWallet
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import com.example.techorbit.ui.activity.HomeActivity
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import kotlinx.coroutines.launch

class HomeViewmodel(val repositary: TechorbitRepositary, context: Context) : ViewModel() {
    private val mutble_livedata = MutableLiveData<ResultOf<MyWallet>>()
    val liveData: LiveData<ResultOf<MyWallet>> = mutble_livedata

    fun getWallet(header: String) = viewModelScope.launch {
        mutble_livedata.value = repositary.getWallet(header)
    }


    val mutableLiveData = MutableLiveData<String>()

    init {
        mutableLiveData.value = TechorbitSharedPreference(context).getValue(KEYS.BALANCE)
    }

    val sharedlivedata: LiveData<String> = mutableLiveData


    private val _data = MutableLiveData<ResultOf<SignupData>>()
    val data: LiveData<ResultOf<SignupData>> = _data

    fun signup(username: String?, password: String?, deviceId: String?) = viewModelScope.launch {

        _data.value = repositary.signUp(username, password, deviceId)
    }

}
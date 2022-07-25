package com.example.techorbit.viewmodel

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class LoginViewModel(val repositary: TechorbitRepositary) : ViewModel(), Observable {
    @Bindable
    val username = MutableLiveData<String>()

    @Bindable
    val password = MutableLiveData<String>()

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
package com.example.techorbit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.search.ModelSearch
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class SearchViewmodel(val repositary: TechorbitRepositary):ViewModel() {

    private val mutableLiveData = MutableLiveData<ResultOf<ModelSearch>>()
    val liveData: LiveData<ResultOf<ModelSearch>> = mutableLiveData
    fun searchData(header: String, number:String) = viewModelScope.launch {

       mutableLiveData.value=repositary.searchData(header,number)

    }


}
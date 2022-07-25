package com.example.techorbit.ui.fragment.live

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.db.OtarRecharge.OtarRecharge
import com.example.techorbit.db.ScratchCards
import com.example.techorbit.db.TechorbitDb
import com.example.techorbit.model.five.Live
import com.example.techorbit.model.five.Recharge
import com.example.techorbit.model.prepaidplans.Data
import com.example.techorbit.model.wallet.MyWallet
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class LiveViewmodel(val repositary: TechorbitRepositary, val context: Context) : ViewModel() {


    private val mutble_livedata = MutableLiveData<ResultOf<Live>>()
    val liveData: LiveData<ResultOf<Live>> = mutble_livedata

    fun getLiveData(header: String, serivice: String) = viewModelScope.launch {
        mutble_livedata.value = repositary.getLiveData(header, serivice)
    }

    val db = TechorbitDb.getInstance(context)
    val dblivedata = db.scratchCardDao().getAllData()
    val dbreportlivedata = db.otarRecharge().getAllReportData()

    fun saveData(list: List<ScratchCards>) = viewModelScope.launch {
        db.scratchCardDao().insertData(list)
    }

    fun saveReportData(data: OtarRecharge) = viewModelScope.launch {
        db.otarRecharge().insertReportData(data)
    }

    private val scrachcardMutableLiveData = MutableLiveData<ResultOf<Any>>()
    val scrachcarLiveData:LiveData<ResultOf<Any>> = scrachcardMutableLiveData

    fun getInventory(head: String) = viewModelScope.launch {
        scrachcardMutableLiveData.value = repositary.getInventoryReports(head)
    }

    fun clearAll()=viewModelScope.launch{
        db.scratchCardDao().clearAll()
    }

    fun clearAllReport()=viewModelScope.launch{
        db.otarRecharge().clearAllReport()
    }

    fun updateStatus(id:Int,status:String) = viewModelScope.launch {
        db.otarRecharge().updateStatus(id,status)
    }

    fun deleteSingleData(id: String) = viewModelScope.launch {
        db.otarRecharge().deleteSingleData(id)
    }

}
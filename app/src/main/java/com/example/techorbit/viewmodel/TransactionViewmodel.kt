package com.example.techorbit.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techorbit.model.TransactionModel
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.launch

class TransactionViewmodel(private val repositary: TechorbitRepositary) : ViewModel() {
    private val mutableLiveData = MutableLiveData<ResultOf<Any>>()
    val liveData: LiveData<ResultOf<Any>> = mutableLiveData
    fun getTransactionReports(head: String, start: String, end: String) = viewModelScope.launch {
//        mutableLiveData.value = repositary.getTransaction(head, start, end)
        mutableLiveData.value = repositary.getTransactioReport(head, start, end)
    }

}
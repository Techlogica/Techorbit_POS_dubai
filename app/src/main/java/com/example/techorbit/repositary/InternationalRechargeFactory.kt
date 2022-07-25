package com.example.techorbit.repositary

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.viewmodel.RechargePlanViewModel
import java.lang.IllegalArgumentException

class InternationalRechargeFactory(val repositary: BaseRepositary,val context: Context):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return when{
            modelClass.isAssignableFrom(RechargePlanViewModel::class.java) -> RechargePlanViewModel(repositary as TechorbitRepositary,context) as T
            else -> throw IllegalArgumentException("Viewmodel class not found")
        }

    }

}
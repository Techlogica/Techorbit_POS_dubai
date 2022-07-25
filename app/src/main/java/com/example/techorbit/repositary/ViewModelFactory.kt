package com.example.techorbit.repositary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.viewmodel.*
import java.lang.IllegalArgumentException

class ViewModelFactory(val repositary: BaseRepositary) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repositary as TechorbitRepositary) as T
            modelClass.isAssignableFrom(CountryViewmodel::class.java) -> CountryViewmodel(repositary as TechorbitRepositary) as T
            modelClass.isAssignableFrom(OtarDuViewmodel::class.java) -> OtarDuViewmodel(repositary as TechorbitRepositary) as T
//            modelClass.isAssignableFrom(RechargePlanViewModel::class.java) -> RechargePlanViewModel(repositary as TechorbitRepositary) as T
            modelClass.isAssignableFrom(InternationalRechargeViewmodel::class.java) -> InternationalRechargeViewmodel(repositary as TechorbitRepositary) as T
            else -> throw IllegalArgumentException("Viewmodel class not found")
        }
    }
}
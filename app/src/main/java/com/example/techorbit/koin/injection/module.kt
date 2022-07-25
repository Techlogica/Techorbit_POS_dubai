package com.example.techorbit.koin.injection

import com.example.techorbit.repositary.BaseRepositary
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.TechorbitClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<HelloRepositary> { HelloRepositaryImpl() }
     factory { TechorbitRepositary(get())}
     single { getApi() }

}


fun getApi(): TechorbitClient {
    return RemoteDataSource().buildApi(TechorbitClient::class.java)
}

fun getApiTest(): TechorbitClient {
    return RemoteDataSource().buildApi(TechorbitClient::class.java)
}

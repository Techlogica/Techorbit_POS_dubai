package com.example.techorbit.repositary

import com.example.techorbit.services.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

abstract class BaseRepositary {
    suspend fun <T> safeApi(apiCall: suspend () -> T): ResultOf<T> =
        withContext(Dispatchers.IO) {
            try {
                ResultOf.Succes(apiCall.invoke())
            } catch (t: Throwable) {
                when (t) {

                    is SocketTimeoutException-> ResultOf.Failiure(false, 408, null,"time out")

                    is HttpException -> {

                        ResultOf.Failiure(false, t.code(),t.response()?.errorBody(),t.message())
                    }
                    else -> {

                        ResultOf.Failiure(true, null, null,"no network")
                    }
                }
            }
        }

}
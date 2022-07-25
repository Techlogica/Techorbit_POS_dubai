package com.example.techorbit.services

import android.net.sip.SipErrorCode
import okhttp3.ResponseBody
import retrofit2.http.Body

sealed class ResultOf<out T> {
    data class Succes<out T>(val result: T) : ResultOf<T>()
    data class Failiure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val message: String
    ) : ResultOf<Nothing>()


}
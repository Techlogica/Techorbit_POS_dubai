package com.example.techorbit.services

import com.example.techorbit.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteDataSource {

    private val baseUrl = "http://recharge.techorbit.net:44396/api/v1/"
//    private val baseUrl = " https://api.techlogica.com/techorbit/api/v1/"
//    private  val baseUrl = "http://192.168.101.14/techorbit/api/v1/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    private val client = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            this.addInterceptor(loggingInterceptor)
        }
        this.connectTimeout(108, TimeUnit.SECONDS)
        this.readTimeout(108, TimeUnit.SECONDS)
        this.writeTimeout(108, TimeUnit.SECONDS)
    }.build()


    private val rechargeClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            this.addInterceptor(loggingInterceptor)
        }
        this.connectTimeout(30, TimeUnit.SECONDS)
        this.readTimeout(30, TimeUnit.SECONDS)
        this.writeTimeout(30, TimeUnit.SECONDS)
    }.build()



    fun <API> buildApi(api: Class<API>): API {
        return Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }

    fun <API> buildRechargeApi(api: Class<API>): API {
        return Retrofit.Builder().baseUrl(baseUrl).client(rechargeClient)
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }
}

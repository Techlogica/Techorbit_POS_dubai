package com.example.techorbit.services

import com.example.techorbit.model.TransactionModel
import com.example.techorbit.model.confirm.ConfirmScratch
import com.example.techorbit.model.confirmresponse.ConfirmResponse
import com.example.techorbit.model.country.CountryList
import com.example.techorbit.model.five.Live
import com.example.techorbit.model.international.InternationalRechargeModel
import com.example.techorbit.model.me.ModelCommission
import com.example.techorbit.model.notification.ModelNotifications
import com.example.techorbit.model.operator.Operators
import com.example.techorbit.model.otarrecharge.DoOtarRecharge
import com.example.techorbit.model.prepaidoperator.PrepaidOperator
import com.example.techorbit.model.prepaidplans.PrepaidPlans
import com.example.techorbit.model.purchase.ModelPurchase
import com.example.techorbit.model.reciept.RecieptReport
import com.example.techorbit.model.reports.inventory.ModelInventory
import com.example.techorbit.model.reports.terminal.ModelTerminalRepors
import com.example.techorbit.model.reports.vendor.ModelVendror
import com.example.techorbit.model.reports.walletreports.ModelWallet
import com.example.techorbit.model.retriverecharge.RetriveRecharge
import com.example.techorbit.model.scratchcardrecharge.ScratchCard
import com.example.techorbit.model.search.ModelSearch
import com.example.techorbit.model.signup.SignupData
import com.example.techorbit.model.traceid.ModeTraceId
import com.example.techorbit.model.wallet.MyWallet
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface TechorbitClient {
    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("Username") username: String?,
        @Field("Password") password: String?,
        @Field("DeviceId") deviceId: String?
    ): SignupData

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("countryList")
    suspend fun getCountries(
        @Header("Authorization") auth: String,
        @Body map: HashMap<String, String>
    ): CountryList

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("prepaidOperator")
    suspend fun getPrepaidOperator(
        @Header("Authorization") auth: String,
        @Body map: HashMap<String, String>
    ): PrepaidOperator

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("getOperatorList")
    suspend fun getOperatorList(
        @Header("Authorization") auth: String,
        @Query("beneficiarycountryId") beneficiaryId: String
    ): Operators


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("doOTARRecharge")
    suspend fun doOtarRecharge(
        @HeaderMap hashMap: HashMap<String, String>,
        @Body map: HashMap<String, String>
    ): DoOtarRecharge

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("prepaidPlans")
    suspend fun getPrepaidPlans(@Header("Authorization") auth: String): PrepaidPlans

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("doPrepaidRecharge")
    suspend fun internationalRecharge(
        @HeaderMap header: HashMap<String,String>,
        @Body map: HashMap<String, String>
    ): InternationalRechargeModel


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("ConfirmScratchCard")
    suspend fun confirmScratchCard(
        @Header("Authorization") auth: String,
        @Body confirm: ConfirmScratch
    ): ConfirmResponse


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("ScratchCardRecharge")
    suspend fun scratchCardRecharge(
        @HeaderMap header: HashMap<String,String?>,
        @Body map: HashMap<String, String?>
    ): ScratchCard


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("myWallet")
    suspend fun getWallet(@Header("Authorization") auth: String): MyWallet

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("ScratchCardPlans")
    suspend fun getLiveData(
        @Header("Authorization") auth: String,
        @Query("service") service: String
    ): Live

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("TransactionReportByBeneficiary")
    suspend fun getSearchData(
        @Header("Authorization") auth: String,
        @Query("beneficiaryNumber") service: String
    ): ModelSearch

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("WalletReport")
    suspend fun getWalletReport(
        @Header("Authorization") auth: String,
        @Query("startDate") start: String, @Query("endDate") end: String
    ): ModelWallet

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("SalesReport")
    suspend fun getTerminalReport(
        @Header("Authorization") auth: String,
        @Query("startDate") start: String, @Query("endDate") end: String
    ): ModelTerminalRepors

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("VendorOutstandingReport")
    suspend fun getVendor(
        @Header("Authorization") auth: String,
        @Query("startDate") start: String, @Query("endDate") end: String
    ): ModelVendror

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("RechargeReport")
    suspend fun getTransaction(
        @Header("Authorization") auth: String,
        @Query("startDate") start: String, @Query("endDate") end: String
    ): Any

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("PurchaseReport")
    suspend fun getPurchase(
        @Header("Authorization") auth: String,
        @Query("startDate") start: String, @Query("endDate") end: String
    ): ModelPurchase

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("getAllNotifications")
    suspend fun getNotifications(
        @Header("Authorization") header: String,
        @Query("isUnReadOnly") unread: String
    ): ModelNotifications

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("AllScratchCardPlans")
    suspend fun getInventory(@Header("Authorization") header: String): Any


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("ReceiptReport")
    suspend fun getRecieptReport(
        @Header("Authorization") auth: String,
        @Query("startDate") start: String, @Query("endDate") end: String
    ): RecieptReport

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("me")
    suspend fun getCommissionProfile(
        @Header("Authorization") auth: String
    ): ModelCommission


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("AllScratchCardPlans")
    fun getTest(@Header("Authorization") header: String): Call<Any>


    @POST("RechargeReportTerminal")
    suspend fun getTransactionReport(
        @Header("Authorization") auth: String,
        @Query("startDate") start: String, @Query("endDate") end: String
    ): Any


    @POST("RechargeReportTerminal")
    suspend fun rechargeTerminalReport(
        @Header("Authorization") auth: String,
        @Query("startDate") start: String, @Query("endDate") end: String
    ): Any

    @POST("RetrieveRecharge")
    suspend fun retrieveRecharge(
        @Header("Authorization") auth: String,
        @Body map: HashMap<String, String?>
    ):RetriveRecharge

    @POST("RetrieveCardRecharge")
    suspend fun retrieveCardRecharge(
        @Header("Authorization") auth: String,
        @Body map: HashMap<String, String?>
    ):ScratchCard

    @POST("CheckMrp")
    suspend fun checkMrp(
        @Header("Authorization") auth: String, @Query("sendValue")sendvalue:String,
        @Query("denominationId")denominationId:String): ModeTraceId

}
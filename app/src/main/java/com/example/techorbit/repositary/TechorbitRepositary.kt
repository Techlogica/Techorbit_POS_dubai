package com.example.techorbit.repositary

import com.example.techorbit.services.TechorbitClient

class TechorbitRepositary(private val client: TechorbitClient) : BaseRepositary() {
    suspend fun signUp(username: String?, password: String?, deviceId: String?) =
        safeApi { client.login(username, password, deviceId) }

    suspend fun getCountries(header: String, map: HashMap<String, String>) =
        safeApi { client.getCountries(header, map) }

    suspend fun getPrepaidOperator(header: String, map: HashMap<String, String>) =
        safeApi { client.getPrepaidOperator(header, map) }

    suspend fun doRecharge(header: HashMap<String,String>, map: HashMap<String, String>) =
        safeApi { client.doOtarRecharge(header, map) }

    suspend fun getPrepaidPlans(header: String) =
        safeApi { client.getPrepaidPlans(header) }

    suspend fun getPrepaidoperatorList(header: String,beneficiaryId:String) =
        safeApi { client.getOperatorList(header,beneficiaryId) }

    suspend fun internationalRecharge(header: HashMap<String,String>, map: HashMap<String, String>) =
        safeApi { client.internationalRecharge(header, map) }

    suspend fun scratchCardRecharge(header: HashMap<String,String?>, map: HashMap<String, String?>) =
        safeApi { client.scratchCardRecharge(header, map) }

    suspend fun getWallet(header: String) =
        safeApi { client.getWallet(header) }

    suspend fun getLiveData(header: String,service:String) =
        safeApi { client.getLiveData(header,service) }

    suspend fun searchData(header: String,search:String) =
        safeApi { client.getSearchData(header,search) }

    suspend fun getWalletReports(header: String,start:String,end:String) =
        safeApi { client.getWalletReport(header,start,end) }
    suspend fun getSalesReports(header: String,start:String,end:String) =
        safeApi { client.getTerminalReport(header,start,end) }

    suspend fun getVendorReports(header: String,start:String,end:String) =
        safeApi { client.getVendor(header,start,end) }

    suspend fun getTransaction(header: String,start:String,end:String) =
        safeApi { client.getTransaction(header,start,end) }

    suspend fun getTransactioReport(header: String,start:String,end:String) =
        safeApi { client.getTransactionReport(header,start,end) }

    suspend fun getPurchase(header: String,start:String,end:String) =
        safeApi { client.getPurchase(header,start,end) }

    suspend fun getNotifications(header: String) =
        safeApi { client.getNotifications(header,unread = "false") }

    suspend fun getInventoryReports(header: String) =
        safeApi { client.getInventory(header) }

    suspend fun getRecieptReport(head: String, start: String, end: String)=
        safeApi { client.getRecieptReport(head,start,end) }
    suspend fun getCommissionRate(head: String)=
        safeApi { client.getCommissionProfile(head) }

    suspend fun checkRechargeStatus(header: String,map: HashMap<String, String?>)=
        safeApi { client.retrieveRecharge(header,map) }

    suspend fun checkCardRechargeStatus(header: String,map: HashMap<String, String?>)=
        safeApi { client.retrieveCardRecharge(header,map) }

    suspend fun checkMrp(header: String,sendvalue:String,denominationId:String)=
        safeApi { client.checkMrp(header,sendvalue,denominationId) }

}

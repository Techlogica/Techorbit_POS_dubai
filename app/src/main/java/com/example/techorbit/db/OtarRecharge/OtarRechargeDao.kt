package com.example.techorbit.db.OtarRecharge

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.techorbit.model.scratchcardrecharge.Data

@Dao
interface OtarRechargeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportData(datas: OtarRecharge)

    @Query("select * from transaction_report")
    fun getAllReportData():LiveData<List<OtarRecharge>>

    @Query("delete from transaction_report")
    suspend fun clearAllReport()

    @Query("update transaction_report set rechargeStatus=:status where rechargeId=:id ")
    fun updateStatus(id: Int,status: String)

    @Query("delete from transaction_report where traceId=:traceId")
    suspend fun deleteSingleData(traceId: String)
}
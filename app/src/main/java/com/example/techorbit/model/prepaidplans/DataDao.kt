package com.example.techorbit.model.prepaidplans

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(datas: List<Data>)

    @Query("select * from data")
    fun getAllData(): LiveData<List<Data>>

    @Query("delete from data")
    suspend fun clearAll()

}
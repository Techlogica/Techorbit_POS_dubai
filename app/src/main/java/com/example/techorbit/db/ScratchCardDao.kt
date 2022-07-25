package com.example.techorbit.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techorbit.model.prepaidplans.Data

@Dao
interface ScratchCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(datas: List<ScratchCards>)

    @Query("select * from scrachcard")
    fun getAllData(): LiveData<List<ScratchCards>>

    @Query("delete from scrachcard")
   suspend fun clearAll()
}
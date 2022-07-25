package com.example.techorbit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.techorbit.db.OtarRecharge.OtarRecharge
import com.example.techorbit.db.OtarRecharge.OtarRechargeDao
import com.example.techorbit.model.prepaidplans.Data
import com.example.techorbit.model.prepaidplans.DataDao

@Database(entities = [Data::class,ScratchCards::class,OtarRecharge::class], version = 3, exportSchema = false)
abstract class TechorbitDb : RoomDatabase() {

    abstract fun datadao(): DataDao
    abstract fun scratchCardDao():ScratchCardDao
    abstract fun otarRecharge():OtarRechargeDao
    companion object {
        // for singleton instantiation

        @Volatile
        private var instance: TechorbitDb? = null

        fun getInstance(context: Context?): TechorbitDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context!!).also { instance = it }

            }
        }

        fun buildDatabase(context: Context): TechorbitDb {

            return Room.databaseBuilder(context, TechorbitDb::class.java, DATABASENAME).addMigrations(
                MIGRATION_1_2, MIGRATION_2_3).allowMainThreadQueries().build()

        }

        private val MIGRATION_1_2=object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
              database.execSQL("ALTER TABLE data ADD COLUMN maxValue TEXT ")
              database.execSQL("ALTER TABLE data ADD COLUMN minValue TEXT ")
            }

        }

        private val MIGRATION_2_3=object :Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE data ADD COLUMN commisionPer INT ")
            }

        }
    }

}
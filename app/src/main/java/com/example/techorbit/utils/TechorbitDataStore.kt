package com.example.techorbit.utils

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TechorbitDataStore(context: Context) {
//    private val  applicationcontext=context.applicationContext
//    private val dataStore: DataStore<Preferences>
//    init {
//        dataStore=applicationcontext.createDataStore(name = "app_preference")
//    }
//
//    val bookmark: Flow<String?>
//        get() = dataStore.data.map { preferences ->
//            preferences[KEY_BOOKMARK]
//        }
////
////    fun<T> getData(key:T):Flow<String>{
////        return dataStore.data.map {preference->preference[key]
////        }
////    }
//
//    suspend fun saveData(value:String){
//        dataStore.edit {preferece->
//            preferece[KEY_BOOKMARK]=value
//        }
//    }
//    companion object {
//        val KEY_BOOKMARK = preferencesKey<String>("key_bookmark")
//    }
}
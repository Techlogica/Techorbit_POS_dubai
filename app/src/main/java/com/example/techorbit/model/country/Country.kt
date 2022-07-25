package com.example.techorbit.model.country

data class CountryList(
    val count: Int,
    val data: List<Data>,
    val error_message: String,
    val flushStatus: Int,
    val response_code: Int,
    val syncVersion: Int
)
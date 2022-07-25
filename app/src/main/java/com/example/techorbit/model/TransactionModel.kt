package com.example.techorbit.model

data class TransactionModel(
    val currencyAbbreviation: String,
    val data: List<Data>,
    val error_message: String,
    val response_code: Int,
    val total: Total
)
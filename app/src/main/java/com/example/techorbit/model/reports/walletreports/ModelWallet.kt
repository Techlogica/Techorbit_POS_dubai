package com.example.techorbit.model.reports.walletreports

data class ModelWallet(
    val currencyAbbreviation: String,
    val `data`: List<Data>,
    val error_message: String,
    val flushStatus: Int,
    val response_code: Int,
    val syncVersion: Int
)
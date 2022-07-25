package com.example.techorbit.model.wallet

data class MyWallet(
    val balance: String,
    val creditLimit: String,
    val currencyAbbreviation: String,
    val error_message: String,
    val flushStatus: Int,
    val openingBalance: String,
    val outstandingCredit: String,
    val response_code: Int,
    val syncVersion: Int
)
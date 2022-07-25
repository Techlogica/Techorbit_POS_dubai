package com.example.techorbit.model.signup

data class Wallet(
    val balance: String,
    val creditLimit: String,
    val currencyAbbreviation: String,
    val openingBalance: String,
    val outstandingCredit: String
)
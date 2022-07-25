package com.example.techorbit.model.reports.walletreports

data class Data(
    val amount: String,
    val commissionId: String,
    val createdTime: String,
    val creditDebit: String,
    val narration: String,
    val rechargeId: String,
    val transactionId: Int,
    val transactionMode: String,
    val transactionType: String
)
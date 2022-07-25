package com.example.techorbit.model.reciept

data class Data(
    val amount: String,
    val date: String,
    val distributorCode: String,
    val mode: String,
    val paidTo: String,
    val receiptId: Int,
    val service: String
)
package com.example.techorbit.model.reciept

data class RecieptReport(
    val currencyAbbreviation: String,
    val `data`: List<Data>,
    val error_message: String,
    val flushStatus: Int,
    val receiptAmountTotal: String,
    val response_code: Int,
    val syncVersion: Int
)
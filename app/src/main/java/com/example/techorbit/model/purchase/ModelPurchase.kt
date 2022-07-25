package com.example.techorbit.model.purchase

data class ModelPurchase(
    val count: Count,
    val currencyAbbreviation: String,
    val `data`: List<Data>,
    val error_message: String,
    val flushStatus: Int,
    val response_code: Int,
    val syncVersion: Int,
    val totalAmount: TotalAmount
)
data class TotalAmount(
    val credit: String,
    val debit: String
)
data class Data(
    val amount: String,
    val createdTime: String,
    val narration: String,
    val transactionId: Int,
    val transactionMode: String,
    val transactionType: String
)

data class Count(
    val credit: Int,
    val debit: Int
)
package com.example.techorbit.model.confirmresponse

data class ConfirmResponse(
    val batchId: Int,
    val `data`: Data,
    val error_message: String,
    val flushStatus: Int,
    val operatingCurrency: String,
    val response_code: Int,
    val syncVersion: Int
)
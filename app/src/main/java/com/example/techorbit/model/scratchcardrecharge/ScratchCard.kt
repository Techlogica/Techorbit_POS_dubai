package com.example.techorbit.model.scratchcardrecharge

data class ScratchCard(
    val `data`: List<Data>,
    val error_message: String,
    val sub_message: String,
    val flushStatus: Int,
    val quantity: String,
    val response_code: Int,
    val syncVersion: Int,
    val total: Int,
    val transactionId: String
)
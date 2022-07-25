package com.example.techorbit.model.reports.terminal

data class ModelTerminalRepors(
    val `data`: List<Data>,
    val error_message: String,
    val flushStatus: Int,
    val operatingCurrency: String,
    val response_code: Int,
    val syncVersion: Int,
    val total: Total
)
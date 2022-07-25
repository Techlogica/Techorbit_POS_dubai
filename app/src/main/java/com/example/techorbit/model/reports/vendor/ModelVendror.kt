package com.example.techorbit.model.reports.vendor

data class ModelVendror(
    val closingBalance: String,
    val currencyAbbreviation: String,
    val `data`: List<Data>,
    val error_message: String,
    val flushStatus: Int,
    val openingBalance: String,
    val response_code: Int,
    val syncVersion: Int,
    val totalCredit: String,
    val totalDebit: String
)
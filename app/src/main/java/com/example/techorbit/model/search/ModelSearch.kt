package com.example.techorbit.model.search

data class ModelSearch(
    val currencyAbbreviation: String,
    val `data`: List<Data>,
    val error_message: String,
    val flushStatus: Int,
    val response_code: Int,
    val response_from: String,
    val syncVersion: Int,
    val total: Total
)

data class Data(
    val beneficiaryCountry: String,
    val beneficiaryNumber: String,
    val commissionAmount: String,
    val createdTime: String,
    val `operator`: String,
    val rechargeAmount: String,
    val rechargeId: Int,
    val rechargeStatus: String,
    val rechargeType: String
)

data class Total(
    val commissionAmount: Double,
    val rechargeAmount: Int,
    val rechargeCount: Int
)
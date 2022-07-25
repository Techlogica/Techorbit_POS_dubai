package com.example.techorbit.model.reports.rechargeterminal

data class RechargeTerminalSales(
    val currencyAbbreviation: String,
    val `data`: List<Data>,
    val error_message: String,
    val response_code: Int,
    val total: Total
)

data class Data(
    val beneficiaryCountry: String,
    val beneficiaryNumber: String,
    val commissionAmount: Double,
    val createdTime: String,
    val `operator`: String,
    val rechargeAmount: Int,
    val rechargeId: Int,
    val rechargeStatus: String,
    val rechargeType: String,
    val type: String
)

data class Total(
    val commissionAmount: Double,
    val rechargeAmount: Double,
    val rechargeCount: Int
)
package com.example.techorbit.model.five

data class Live(
    val `data`: Data,
    val downlodedRechargeCount: Int,
    val error_message: String,
    val flushStatus: Int,
    val operatingCurrency: String,
    val rechargeCount: Int,
    val rechargeSubTypes1: RechargeSubTypes1,
    val rechargeSubTypes2: List<String>,
    val response_code: Int,
    val service: String,
    val syncVersion: Int
)
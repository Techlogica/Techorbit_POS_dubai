package com.example.techorbit.model.prepaidoperator

data class PrepaidOperator(
    val circleId: Int,
    val circleName: String,
    val circles: Any,
    val circlesCount: Int,
    val error_message: String,
    val flushStatus: Int,
    val hotPlans: List<Any>,
    val hotPlansCount: Int,
    val localCurrency: String,
    val operatingCurrency: String,
    val operatorId: Int,
    val operatorName: String,
    val response_code: Int,
    val syncVersion: Int
)
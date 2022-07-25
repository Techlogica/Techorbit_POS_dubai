package com.example.techorbit.model.retriverecharge

data class RetriveRecharge(
    val error_message: String,
    val response_code: Int,
    val status: String,
    val rechargeId:Int?

)
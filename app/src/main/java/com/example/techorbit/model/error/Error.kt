package com.example.techorbit.model.error

data class Error(
    val error_message: String?,
    val response_code: Int?,
    val sub_message: String?
)
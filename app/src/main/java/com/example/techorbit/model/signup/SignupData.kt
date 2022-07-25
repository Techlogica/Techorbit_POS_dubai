package com.example.techorbit.model.signup

data class SignupData(
    val error_message: String,
    val isActivated: Int,
    val menu: List<Menu>,
    val refresh_token: String,
    val response_code: Int,
    val token: String,
    val user: User,
    val terminal_id:String
)
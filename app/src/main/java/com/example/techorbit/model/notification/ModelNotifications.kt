package com.example.techorbit.model.notification

data class ModelNotifications(
    val count: Int,
    val `data`: List<Data>,
    val error_message: String,
    val response_code: Int
)

data class Data(
    val dateSent: String,
    val message: String,
    val notificationId: Int,
    val status: String,
    val title: String
)
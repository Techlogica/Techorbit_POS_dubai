package com.example.techorbit.model.confirmresponse

data class Data(
    val commission: String,
    val grandTotal: String,
    val products: List<Product>,
    val total: String
)
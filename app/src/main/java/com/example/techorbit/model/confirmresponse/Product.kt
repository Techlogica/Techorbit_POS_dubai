package com.example.techorbit.model.confirmresponse

data class Product(
    val detail: List<Detail>,
    val mrp: String,
    val name: String
)
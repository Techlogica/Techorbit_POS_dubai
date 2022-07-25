package com.example.techorbit.model.reports.inventory

data class ModelInventory(
    val count: Int,
    val error_message: String,
    val etisalatCard: EtisalatCard,
    val fiveCard: FiveCard,
    val flushStatus: Int,
    val helloCard: HelloCard,
    val operatingCurrency: String,
    val products: List<String>,
    val response_code: Int,
    val salikCard: SalikCard,
    val syncVersion: Int
)

data class Data(
    val available: Int,
    val benefits: String,
    val localAmount: String,
    val localCurrency: String,
    val mrp: String,
    val name: String,
    val planId: Int,
    val stock: Int
)
data class DataX(
    val available: Int,
    val benefits: String,
    val localAmount: String,
    val localCurrency: String,
    val mrp: String,
    val name: String,
    val planId: Int,
    val stock: Int
)

data class DataXX(
    val available: Int,
    val benefits: String,
    val localAmount: String,
    val localCurrency: String,
    val mrp: String,
    val name: String,
    val planId: Int,
    val stock: Int
)
data class DataXXX(
    val available: Int,
    val benefits: String,
    val localAmount: String,
    val localCurrency: String,
    val mrp: String,
    val name: String,
    val planId: Int,
    val stock: Int
)
data class EtisalatCard(
    val bg: String,
    val `data`: List<Data>,
    val name: String
)

data class SalikCard(
    val bg: String,
    val `data`: List<DataXXX>,
    val name: String
)
data class HelloCard(
    val bg: String,
    val `data`: List<DataXX>,
    val name: String
)
data class FiveCard(
    val bg: String,
    val `data`: List<DataX>,
    val name: String
)
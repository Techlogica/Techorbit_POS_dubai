package com.example.techorbit.model.reports.inventory

import androidx.room.Entity
data class Inventory(
    val name: String,
    val faceValue: String,
    val stock: String
)
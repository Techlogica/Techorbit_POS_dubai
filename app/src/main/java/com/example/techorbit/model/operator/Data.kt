package com.example.techorbit.model.operator

data class Data(
    val name: String,
    val operatorId: Int
){
    override fun toString(): String {
        return name.toString()
    }
}
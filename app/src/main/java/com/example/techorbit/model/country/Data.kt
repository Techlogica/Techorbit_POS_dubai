package com.example.techorbit.model.country

data class Data(
    val abbreviation: String,
    val beneficiaryCountry: Any,
    val currency: String,
    val currencyAbbreviation: String,
    val customerCareContact: Any,
    val dingPlanOperatingCountry: Any,
    val extension: Int,
    val flagName: String?=null,
    val id: Int,
    val isOperatingCountry: Int,
    val name: String,
    val numberLength: Int,
    val scratchCardPlansBeneficiary: Any,
    val scratchCardPlansOperating: Any,
    val status: Int
)
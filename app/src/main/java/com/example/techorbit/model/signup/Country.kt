package com.example.techorbit.model.signup

data class Country(
    val abbreviation: String,
    val countryId: Int,
    val currency: String,
    val currencyAbbreviation: String,
    val extension: Int,
    val flagName: String,
    val name: String,
    val numberLength: Int
)
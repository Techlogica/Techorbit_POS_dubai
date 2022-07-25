package com.example.techorbit.model.signup

data class User(
    val country: Country?=null,
    val deviceKey: String?=null,
    val distributorCode: Any?=null,
    val emailId: Any?=null,
    val emiratesId: String?=null,
    val firstName: String?=null,
    val gender: String?=null,
    val isActivated: Int?=null,
    val isTabletUser: Int?=null,
    val lastName: String?=null,
    val m2MId: String?=null,
    val name: String?=null,
    val nationality: Any?=null,
    val nearbyLandmark: Any?=null,
    val notifications: Notifications?=null,
    val parentContact: String?=null,
    val phoneLandline: Any?=null,
    val phoneMobile: String?=null,
    val printTitle: String?=null,
    val proofGiven: Any?=null,
    val shopAddress: String?=null,
    val shopName: String?=null,
    val userType: String?=null,
    val username: String?=null,
    val wallet: Wallet?=null
)
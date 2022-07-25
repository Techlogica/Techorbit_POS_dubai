package com.example.techorbit.model.me

data class ModelCommission(
    val error_message: String,
    val flushStatus: Int,
    val rechargeTimeout: Int,
    val response_code: Int,
    val syncVersion: Int,
    val uaeOperators: UaeOperators,
    val user: User
)
data class CommissionRate(
    val commissionRate: String,
    val rechargeType: String
)
data class Country(
    val abbreviation: String,
    val countryId: Int,
    val currency: String,
    val currencyAbbreviation: String,
    val customerCareContact: String,
    val extension: Int,
    val flagName: String,
    val name: String,
    val numberLength: Int
)
data class Notifications(
    val unreadCount: Int
)
data class Service(
    val id: Int,
    val name: String
)
data class UaeOperators(
    val du: List<String>,
    val etisalat: List<String>
)
data class Update(
    val isCritical: String,
    val isUpdate: String,
    val updateURL: String
)
data class User(
    val commissionRates: List<CommissionRate>,
    val country: Country,
    val distributorCode: Any,
    val emailId: Any,
    val emiratesId: String,
    val firstName: String,
    val gender: String,
    val isActivated: Int,
    val isProofGiven: Any,
    val isSIMGiven: Any,
    val isTabletUser: String,
    val lastName: String,
    val m2MId: Any,
    val name: String,
    val nationality: Any,
    val nearbyLandmark: Any,
    val notifications: Notifications,
    val parentContact: String,
    val phoneLandline: String,
    val phoneMobile: String,
    val printTitle: Any,
    val proofGiven: Any,
    val serviceList: List<Service>,
    val shopAddress: String,
    val shopName: String,
    val tabletId: String,
    val update: Update,
    val userType: String,
    val username: String,
    val wallet: Wallet
)
data class Wallet(
    val balance: String,
    val creditLimit: String,
    val currencyAbbreviation: String,
    val openingBalance: String,
    val outstandingCredit: String
)
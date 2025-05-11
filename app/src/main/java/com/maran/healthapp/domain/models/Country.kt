package com.maran.healthapp.domain.models

enum class Country(val apiCode: String?) {
    ALL(null),
    US("us"),
    GB("gb"),
    DE("de"),
    RU("ru"),
    IN("in"),
    CN("cn"),
    JP("jp"),
    AU("au");

    companion object {
        fun validateCountry(country: String): Country {
            val enumCategory = Country.values().find { it.apiCode.equals(country, ignoreCase = true) }
            require(enumCategory != null) {
                "Invalid $country country"
            }

            return enumCategory
        }
    }
}
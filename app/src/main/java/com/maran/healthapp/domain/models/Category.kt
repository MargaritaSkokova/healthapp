package com.maran.healthapp.domain.models

enum class Category(val apiName: String) {
    BUSINESS("business"),
    GENERAL("general"),
    ENTERTAINMENT("entertainment"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    companion object {
        fun validateCategory(category: String): Category {
            val enumCategory = Category.values().find { it.apiName.equals(category, ignoreCase = true) }
            require(enumCategory != null) {
                "Invalid $category category"
            }

            return enumCategory
        }
    }
}
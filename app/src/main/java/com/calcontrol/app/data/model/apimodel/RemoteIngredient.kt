package com.calcontrol.app.data.model.apimodel

data class RemoteIngredient(
    val carbohydrate: Double,
    val dietary_fiber: Double,
    val energy_kcal: Double,
    val fat: Double,
    val id: Int,
    val ingredient_name: String,
    val measurement_amount: Int,
    val measurement_unit: String,
    val protein: Double,
    val sugar: Double
)
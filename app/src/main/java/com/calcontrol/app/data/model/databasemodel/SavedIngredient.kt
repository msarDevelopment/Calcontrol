package com.calcontrol.app.data.model.databasemodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "saved_ingredient")
data class SavedIngredient(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ing_id")
    var ing_id: Int?,

    @ColumnInfo(name = "saved_ingredient_name")
    var saved_ingredient_name: String,

    @ColumnInfo(name = "base_measurement_amount")
    var base_measurement_amount: Int,

    @ColumnInfo(name = "measurement_unit")
    var measurement_unit: String,

    @ColumnInfo(name = "base_calories_per_100")
    var base_calories_per_100: Double,

    @ColumnInfo(name = "saved_meal_id")
    var saved_meal_id: Int,

    @ColumnInfo(name = "custom_measurement_amount")
    var custom_measurement_amount: Int,

    @ColumnInfo(name = "custom_calculated_calories")
    var custom_calculated_calories: Double,

    @ColumnInfo(name = "source_id")
    var source_id: Int

) : Serializable





package com.calcontrol.app.data.model.databasemodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "saved_meal")
data class SavedMeal(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "saved_meal_name")
    var saved_meal_name: String,

    @ColumnInfo(name = "total_calories")
    var total_calories: Double,

    @ColumnInfo(name = "plan_id")
    var plan_id: Int

) : Serializable

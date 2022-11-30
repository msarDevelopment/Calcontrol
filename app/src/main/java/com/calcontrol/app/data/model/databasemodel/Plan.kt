package com.calcontrol.app.data.model.databasemodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "plan")
data class Plan(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "plan_name")
    var plan_name: String,

    @ColumnInfo(name = "weight")
    var weight: Int,

    @ColumnInfo(name = "height")
    var height: Int,

    @ColumnInfo(name = "age")
    var age: Int,

    @ColumnInfo(name = "gender")
    var gender: String,

    @ColumnInfo(name = "bmr")
    var bmr: Double,

    @ColumnInfo(name = "bmr_with_activity_factor")
    var bmr_with_activity_factor: Double

) : Serializable
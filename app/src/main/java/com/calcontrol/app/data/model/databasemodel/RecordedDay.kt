package com.calcontrol.app.data.model.databasemodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recorded_day")
data class RecordedDay(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "day_id")
    var day_id: Int?,

    @ColumnInfo(name = "recorded_day_date")
    var recorded_day_date: String,

    @ColumnInfo(name = "plan_id")
    var plan_id: Int,

    @ColumnInfo(name = "calories_in")
    var calories_in: Double,

    @ColumnInfo(name = "calories_out")
    var calories_out: Double

)
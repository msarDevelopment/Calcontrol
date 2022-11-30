package com.calcontrol.app.data.model.databasemodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumed_workout")
data class ConsumedWorkout(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "consumed_workout_name")
    var consumed_workout_name: String,

    @ColumnInfo(name = "total_calories")
    var total_calories: Double,

    @ColumnInfo(name = "day_id")
    var day_id: Int

)
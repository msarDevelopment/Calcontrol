package com.calcontrol.app.data.model.databasemodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "saved_exercise")
data class SavedExercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exr_id")
    var exr_id: Int?,

    @ColumnInfo(name = "saved_exercise_name")
    var saved_exercise_name: String,

    @ColumnInfo(name = "met")
    var met: Double,

    @ColumnInfo(name = "duration")
    var duration: Int,

    @ColumnInfo(name = "calories_per_min")
    var calories_per_min: Double,

    @ColumnInfo(name = "custom_calculated_calories")
    var custom_calculated_calories: Double,

    @ColumnInfo(name = "saved_workout_id")
    var saved_workout_id: Int,

    @ColumnInfo(name = "source_id")
    var source_id: Int

) : Serializable
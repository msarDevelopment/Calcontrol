package com.calcontrol.app.data.model.databasemodel.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.calcontrol.app.data.model.databasemodel.SavedWorkout

@Dao
interface SavedWorkoutDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSavedWorkout(savedWorkout: SavedWorkout)

    @Update
    suspend fun updateSavedWorkout(savedWorkout: SavedWorkout)

    @Delete
    suspend fun deleteSavedWorkout(savedWorkout: SavedWorkout)

    @Query("SELECT * FROM saved_workout ORDER BY id DESC")
    fun getAllSavedWorkouts(): LiveData<List<SavedWorkout>>

    @Query("SELECT * FROM saved_workout WHERE plan_id = :plan_id ORDER BY id DESC")
    fun getAllSavedWorkoutsFromPlan(plan_id: Int): LiveData<List<SavedWorkout>>

    @Query("DELETE FROM saved_workout")
    suspend fun deleteAllSavedWorkouts()

    @Query("SELECT * FROM saved_workout WHERE id = :workout_id")
    fun getSavedWorkoutLiveDataTransformations(workout_id: Int): LiveData<SavedWorkout>

    @Query("SELECT * FROM saved_workout ORDER BY id DESC")
    suspend fun getAllSavedWorkoutsNoLiveData(): MutableList<SavedWorkout>

    @Query("SELECT * FROM saved_workout WHERE plan_id = :plan_id ORDER BY id DESC")
    suspend fun getAllSavedWorkoutsFromPlanNoLiveData(plan_id: Int): MutableList<SavedWorkout>

}
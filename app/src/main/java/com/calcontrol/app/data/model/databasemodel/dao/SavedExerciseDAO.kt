package com.calcontrol.app.data.model.databasemodel.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.calcontrol.app.data.model.databasemodel.SavedExercise

@Dao
interface SavedExerciseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSavedExercise(savedExercise: SavedExercise)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSavedExercise(savedExercise: SavedExercise)

    @Delete
    suspend fun deleteSavedExercise(savedExercise: SavedExercise)

    @Query("SELECT * FROM saved_exercise ORDER BY exr_id DESC")
    fun getAllSavedExercises(): LiveData<List<SavedExercise>>

    @Query("DELETE FROM saved_exercise")
    suspend fun deleteAllSavedExercises()

    @Query("SELECT * FROM saved_exercise INNER JOIN saved_workout ON saved_exercise.saved_workout_id = saved_workout.id WHERE saved_exercise.saved_workout_id = :search_id")
    suspend fun getAllSavedExercisesFromSavedWorkout(search_id: Int): List<SavedExercise>

    @Query("SELECT * FROM saved_exercise INNER JOIN saved_workout ON saved_exercise.saved_workout_id = saved_workout.id WHERE saved_exercise.saved_workout_id = :search_id")
    fun getAllExercisesFromSavedWorkoutLiveDataTransformations(search_id: Int): LiveData<List<SavedExercise>>

    @Query("DELETE FROM saved_exercise WHERE saved_workout_id = :workout_id")
    suspend fun deleteAllExercisesFromSavedWorkout(workout_id: Int)

}
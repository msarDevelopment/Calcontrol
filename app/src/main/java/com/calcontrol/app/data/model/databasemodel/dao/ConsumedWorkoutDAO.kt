package com.calcontrol.app.data.model.databasemodel.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.calcontrol.app.data.model.databasemodel.ConsumedWorkout

@Dao
interface ConsumedWorkoutDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addConsumedWorkout(consumedWorkout: ConsumedWorkout)

    @Update
    suspend fun updateConsumedWorkout(consumedWorkout: ConsumedWorkout)

    @Delete
    suspend fun deleteConsumedWorkout(consumedWorkout: ConsumedWorkout)

    @Query("SELECT * FROM consumed_workout ORDER BY id DESC")
    fun getAllConsumedWorkouts(): LiveData<List<ConsumedWorkout>>

    @Query("DELETE FROM consumed_workout")
    suspend fun deleteAllConsumedWorkouts()

    @Query("SELECT * FROM consumed_workout WHERE id = :workout_id")
    fun getConsumedWorkoutLiveDataTransformations(workout_id: Int): LiveData<ConsumedWorkout>

    @Query("DELETE FROM consumed_workout WHERE day_id = :day_id")
    suspend fun deleteAllConsumedWorkoutsFromDay(day_id: Int)

}
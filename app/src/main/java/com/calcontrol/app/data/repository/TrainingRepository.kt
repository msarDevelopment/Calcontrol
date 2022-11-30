package com.calcontrol.app.data.repository

import androidx.lifecycle.LiveData
import com.calcontrol.app.data.api.RetrofitInstance
import com.calcontrol.app.data.model.apimodel.RemoteExercise
import com.calcontrol.app.data.model.databasemodel.ConsumedWorkout
import com.calcontrol.app.data.model.databasemodel.SavedExercise
import com.calcontrol.app.data.model.databasemodel.SavedWorkout
import com.calcontrol.app.data.model.databasemodel.dao.ConsumedWorkoutDAO
import com.calcontrol.app.data.model.databasemodel.dao.SavedExerciseDAO
import com.calcontrol.app.data.model.databasemodel.dao.SavedWorkoutDAO
import retrofit2.Response

class TrainingRepository(
    private val savedExerciseDao: SavedExerciseDAO,
    private val savedWorkoutDao: SavedWorkoutDAO,
    private val consumedWorkoutDao: ConsumedWorkoutDAO
) {

    fun getAllSavedExercises(): LiveData<List<SavedExercise>> =
        savedExerciseDao.getAllSavedExercises()

    suspend fun addSavedExercise(savedExercise: SavedExercise) {
        savedExerciseDao.addSavedExercise(savedExercise)
    }

    suspend fun updateSavedExercise(savedExercise: SavedExercise) {
        savedExerciseDao.updateSavedExercise(savedExercise)
    }

    suspend fun deleteSavedExercise(savedExercise: SavedExercise) {
        savedExerciseDao.deleteSavedExercise(savedExercise)
    }

    suspend fun deleteAllSavedExercises() {
        savedExerciseDao.deleteAllSavedExercises()
    }

    suspend fun getAllSavedExercisesFromSavedWorkout(search_id: Int): List<SavedExercise> =
        savedExerciseDao.getAllSavedExercisesFromSavedWorkout(search_id)

    // for list of SavedExercises of a SavedWorkout in EditSavedWorkoutDialogFragment
    fun getAllExercisesFromSavedWorkoutLiveDataTransformations(search_id: Int): LiveData<List<SavedExercise>> =
        savedExerciseDao.getAllExercisesFromSavedWorkoutLiveDataTransformations(search_id)

    suspend fun deleteAllExercisesFromSavedWorkout(workout_id: Int) {
        savedExerciseDao.deleteAllExercisesFromSavedWorkout(workout_id)
    }


    fun getAllSavedWorkouts(): LiveData<List<SavedWorkout>> =
        savedWorkoutDao.getAllSavedWorkouts()

    // for list of SavedWorkouts in TrainingFragment
    fun getAllSavedWorkoutsFromPlan(planId: Int): LiveData<List<SavedWorkout>> {
        return savedWorkoutDao.getAllSavedWorkoutsFromPlan(planId)
    }

    suspend fun addSavedWorkout(savedWorkout: SavedWorkout) {
        savedWorkoutDao.addSavedWorkout(savedWorkout)
    }

    suspend fun updateSavedWorkout(savedWorkout: SavedWorkout) {
        savedWorkoutDao.updateSavedWorkout(savedWorkout)
    }

    suspend fun deleteSavedWorkout(savedWorkout: SavedWorkout) {
        savedWorkoutDao.deleteSavedWorkout(savedWorkout)
    }

    suspend fun deleteAllSavedWorkouts() {
        savedWorkoutDao.deleteAllSavedWorkouts()
    }

    fun getSavedWorkoutLiveDataTransformations(workout_id: Int): LiveData<SavedWorkout> =
        savedWorkoutDao.getSavedWorkoutLiveDataTransformations(workout_id)

    suspend fun getAllSavedWorkoutsNoLiveData(): MutableList<SavedWorkout> {
        return savedWorkoutDao.getAllSavedWorkoutsNoLiveData()
    }

    // for list of SavedWorkouts in DashboardFragment's Dialog
    suspend fun getAllSavedWorkoutsFromPlanNoLiveData(plan_id: Int): MutableList<SavedWorkout> {
        return savedWorkoutDao.getAllSavedWorkoutsFromPlanNoLiveData(plan_id)
    }


    // for list of RemoteExercise in SearchExerciseDialogFragment
    suspend fun getNetworkExercises(searchQuery: String): List<RemoteExercise> {

        val response: Response<MutableList<RemoteExercise>>

        var fetchedExercises: MutableList<RemoteExercise> = mutableListOf()

        response = RetrofitInstance.api.searchExercises(searchQuery)

        if (response.isSuccessful && response.body() != null) {
            val exercises = response.body()
            if (exercises != null) {
                fetchedExercises = exercises
                return fetchedExercises
            } else {
                //
            }
        }

        return fetchedExercises

    }


    fun getAllConsumedMeals(): LiveData<List<ConsumedWorkout>> =
        consumedWorkoutDao.getAllConsumedWorkouts()

    suspend fun addConsumedWorkout(consumedWorkout: ConsumedWorkout) {
        consumedWorkoutDao.addConsumedWorkout(consumedWorkout)
    }

    suspend fun updateConsumedWorkout(consumedWorkout: ConsumedWorkout) {
        consumedWorkoutDao.updateConsumedWorkout(consumedWorkout)
    }

    suspend fun deleteConsumedWorkout(consumedWorkout: ConsumedWorkout) {
        consumedWorkoutDao.deleteConsumedWorkout(consumedWorkout)
    }

    suspend fun deleteAllConsumedWorkouts() {
        consumedWorkoutDao.deleteAllConsumedWorkouts()
    }

    suspend fun deleteAllConsumedWorkoutsFromDay(day_id: Int) {
        consumedWorkoutDao.deleteAllConsumedWorkoutsFromDay(day_id)
    }

}
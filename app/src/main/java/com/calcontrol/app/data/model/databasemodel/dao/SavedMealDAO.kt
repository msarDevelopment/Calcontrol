package com.calcontrol.app.data.model.databasemodel.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.calcontrol.app.data.model.databasemodel.SavedMeal

@Dao
interface SavedMealDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSavedMeal(savedMeal: SavedMeal)

    @Update
    suspend fun updateSavedMeal(savedMeal: SavedMeal)

    @Delete
    suspend fun deleteSavedMeal(savedMeal: SavedMeal)

    @Query("SELECT * FROM saved_meal ORDER BY id DESC")
    fun getAllSavedMeals(): LiveData<List<SavedMeal>>

    @Query("SELECT * FROM saved_meal WHERE plan_id = :plan_id ORDER BY id DESC")
    fun getAllSavedMealsFromPlan(plan_id: Int): LiveData<List<SavedMeal>>

    @Query("DELETE FROM saved_meal")
    suspend fun deleteAllSavedMeals()

    @Query("SELECT * FROM saved_meal WHERE id = :meal_id")
    fun getSavedMealLiveDataTransformations(meal_id: Int): LiveData<SavedMeal>

    @Query("SELECT * FROM saved_meal ORDER BY id DESC")
    suspend fun getAllSavedMealsNoLiveData(): MutableList<SavedMeal>

    @Query("SELECT * FROM saved_meal WHERE plan_id = :plan_id ORDER BY id DESC")
    suspend fun getAllSavedMealsFromPlanNoLiveData(plan_id: Int): MutableList<SavedMeal>

}
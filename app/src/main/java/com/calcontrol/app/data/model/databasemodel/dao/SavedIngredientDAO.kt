package com.calcontrol.app.data.model.databasemodel.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.calcontrol.app.data.model.databasemodel.SavedIngredient

@Dao
interface SavedIngredientDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSavedIngredient(savedIngredient: SavedIngredient)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSavedIngredient(savedIngredient: SavedIngredient)

    @Delete
    suspend fun deleteSavedIngredient(savedIngredient: SavedIngredient)

    @Query("SELECT * FROM saved_ingredient ORDER BY ing_id DESC")
    fun getAllSavedIngredients(): LiveData<List<SavedIngredient>>

    @Query("DELETE FROM saved_ingredient")
    suspend fun deleteAllSavedIngredients()

    @Query("SELECT * FROM saved_ingredient INNER JOIN saved_meal ON saved_ingredient.saved_meal_id = saved_meal.id WHERE saved_ingredient.saved_meal_id = :search_id")
    suspend fun getAllIngredientsFromSavedMeal(search_id: Int): List<SavedIngredient>

    @Query("SELECT * FROM saved_ingredient INNER JOIN saved_meal ON saved_ingredient.saved_meal_id = saved_meal.id WHERE saved_ingredient.saved_meal_id = :search_id")
    fun getAllIngredientsFromSavedMealLiveDataTransformations(search_id: Int): LiveData<List<SavedIngredient>>

    @Query("DELETE FROM saved_ingredient WHERE saved_meal_id = :meal_id")
    suspend fun deleteAllIngredientsFromSavedMeal(meal_id: Int)

}
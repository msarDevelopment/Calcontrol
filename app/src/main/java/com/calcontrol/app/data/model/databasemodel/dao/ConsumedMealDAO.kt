package com.calcontrol.app.data.model.databasemodel.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.calcontrol.app.data.model.databasemodel.ConsumedMeal

@Dao
interface ConsumedMealDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addConsumedMeal(consumedMeal: ConsumedMeal)

    @Update
    suspend fun updateConsumedMeal(consumedMeal: ConsumedMeal)

    @Delete
    suspend fun deleteConsumedMeal(consumedMeal: ConsumedMeal)

    @Query("SELECT * FROM consumed_meal ORDER BY id DESC")
    fun getAllConsumedMeals(): LiveData<List<ConsumedMeal>>

    @Query("DELETE FROM consumed_meal")
    suspend fun deleteAllConsumedMeals()

    @Query("SELECT * FROM consumed_meal WHERE id = :meal_id")
    fun getConsumedMealLiveDataTransformations(meal_id: Int): LiveData<ConsumedMeal>

    @Query("DELETE FROM consumed_meal WHERE day_id = :day_id")
    suspend fun deleteAllConsumedMealsFromDay(day_id: Int)

}
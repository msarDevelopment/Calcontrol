package com.calcontrol.app.data.repository

import androidx.lifecycle.LiveData
import com.calcontrol.app.data.api.RetrofitInstance
import com.calcontrol.app.data.model.apimodel.RemoteIngredient
import com.calcontrol.app.data.model.databasemodel.ConsumedMeal
import com.calcontrol.app.data.model.databasemodel.SavedIngredient
import com.calcontrol.app.data.model.databasemodel.SavedMeal
import com.calcontrol.app.data.model.databasemodel.dao.ConsumedMealDAO
import com.calcontrol.app.data.model.databasemodel.dao.SavedIngredientDAO
import com.calcontrol.app.data.model.databasemodel.dao.SavedMealDAO
import retrofit2.Response

class FoodRepository(
    private val savedIngredientDao: SavedIngredientDAO,
    private val savedMealDao: SavedMealDAO,
    private val consumedMealDao: ConsumedMealDAO
) {

    fun getAllSavedIngredients(): LiveData<List<SavedIngredient>> =
        savedIngredientDao.getAllSavedIngredients()

    suspend fun addSavedIngredient(savedIngredient: SavedIngredient) {
        savedIngredientDao.addSavedIngredient(savedIngredient)
    }

    suspend fun updateSavedIngredient(savedIngredient: SavedIngredient) {
        savedIngredientDao.updateSavedIngredient(savedIngredient)
    }

    suspend fun deleteSavedIngredient(savedIngredient: SavedIngredient) {
        savedIngredientDao.deleteSavedIngredient(savedIngredient)
    }

    suspend fun deleteAllSavedIngredients() {
        savedIngredientDao.deleteAllSavedIngredients()
    }

    suspend fun getAllIngredientsFromSavedMeal(search_id: Int): List<SavedIngredient> =
        savedIngredientDao.getAllIngredientsFromSavedMeal(search_id)

    // for list of SavedIngredients of a SavedMeal in EditSavedMealDialogFragment
    fun getAllIngredientsFromSavedMealLiveDataTransformations(search_id: Int): LiveData<List<SavedIngredient>> =
        savedIngredientDao.getAllIngredientsFromSavedMealLiveDataTransformations(search_id)

    suspend fun deleteAllIngredientsFromSavedMeal(meal_id: Int) {
        savedIngredientDao.deleteAllIngredientsFromSavedMeal(meal_id)
    }


    fun getAllSavedMeals(): LiveData<List<SavedMeal>> =
        savedMealDao.getAllSavedMeals()

    // for list of SavedMeals in FoodFragment
    fun getAllSavedMealsFromPlan(planId: Int): LiveData<List<SavedMeal>> {
        return savedMealDao.getAllSavedMealsFromPlan(planId)
    }

    suspend fun addSavedMeal(savedMeal: SavedMeal) {
        savedMealDao.addSavedMeal(savedMeal)
    }

    suspend fun updateSavedMeal(savedMeal: SavedMeal) {
        savedMealDao.updateSavedMeal(savedMeal)
    }

    suspend fun deleteSavedMeal(savedMeal: SavedMeal) {
        savedMealDao.deleteSavedMeal(savedMeal)
    }

    suspend fun deleteAllSavedMeals() {
        savedMealDao.deleteAllSavedMeals()
    }

    fun getSavedMealLiveDataTransformations(meal_id: Int): LiveData<SavedMeal> =
        savedMealDao.getSavedMealLiveDataTransformations(meal_id)

    suspend fun getAllSavedMealsNoLiveData(): MutableList<SavedMeal> {
        return savedMealDao.getAllSavedMealsNoLiveData()
    }

    // for list of SavedMeals in DashboardFragment's Dialog
    suspend fun getAllSavedMealsFromPlanNoLiveData(plan_id: Int): MutableList<SavedMeal> {
        return savedMealDao.getAllSavedMealsFromPlanNoLiveData(plan_id)
    }


    // for list of RemoteIngredient in SearchIngredientDialogFragment
    suspend fun getNetworkIngredients(searchQuery: String): List<RemoteIngredient> {

        val response: Response<MutableList<RemoteIngredient>>

        var fetchedIngredients: MutableList<RemoteIngredient> = mutableListOf()

        response = RetrofitInstance.api.searchIngredients(searchQuery)

        if (response.isSuccessful && response.body() != null) {
            val ingredients = response.body()
            if (ingredients != null) {
                fetchedIngredients = ingredients
                return fetchedIngredients
            } else {
                //
            }
        }

        return fetchedIngredients

    }


    fun getAllConsumedMeals(): LiveData<List<ConsumedMeal>> =
        consumedMealDao.getAllConsumedMeals()

    suspend fun addConsumedMeal(consumedMeal: ConsumedMeal) {
        consumedMealDao.addConsumedMeal(consumedMeal)
    }

    suspend fun updateConsumedMeal(consumedMeal: ConsumedMeal) {
        consumedMealDao.updateConsumedMeal(consumedMeal)
    }

    suspend fun deleteConsumedMeal(consumedMeal: ConsumedMeal) {
        consumedMealDao.deleteConsumedMeal(consumedMeal)
    }

    suspend fun deleteAllConsumedMeals() {
        consumedMealDao.deleteAllConsumedMeals()
    }

    suspend fun deleteAllConsumedMealsFromDay(day_id: Int) {
        consumedMealDao.deleteAllConsumedMealsFromDay(day_id)
    }

}
package com.calcontrol.app.data.model.databasemodel.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.calcontrol.app.data.model.databasemodel.Plan

@Dao
interface PlanDAO {

    @Insert
    suspend fun addPlan(plan: Plan)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlan(plan: Plan)

    @Delete
    suspend fun deletePlan(plan: Plan)

    @Query("SELECT * FROM `plan` ORDER BY id DESC")
    fun getAllPlans(): LiveData<List<Plan>>

    @Query("DELETE FROM `plan`")
    suspend fun deleteAllPlans()

}
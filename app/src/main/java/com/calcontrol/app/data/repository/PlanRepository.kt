package com.calcontrol.app.data.repository

import androidx.lifecycle.LiveData
import com.calcontrol.app.data.model.databasemodel.Plan
import com.calcontrol.app.data.model.databasemodel.dao.PlanDAO

class PlanRepository(private val planDao: PlanDAO) {

    fun getAllPlans(): LiveData<List<Plan>> =
        planDao.getAllPlans()

    suspend fun addPlan(plan: Plan) {
        planDao.addPlan(plan)
    }

    suspend fun updatePlan(plan: Plan) {
        planDao.updatePlan(plan)
    }

    suspend fun deletePlan(plan: Plan) {
        planDao.deletePlan(plan)
    }

    suspend fun deleteAllPlans() {
        planDao.deleteAllPlans()
    }

}
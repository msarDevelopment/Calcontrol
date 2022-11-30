package com.calcontrol.app.ui.plan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.calcontrol.app.data.database.FitnessDatabase
import com.calcontrol.app.data.model.databasemodel.Plan
import com.calcontrol.app.data.repository.PlanRepository
import kotlinx.coroutines.launch

class PlanSelectionViewModel(application: Application) : AndroidViewModel(application) {

    private val planRepository: PlanRepository

    lateinit var plansList: LiveData<List<Plan>>

    init {
        val planDao = FitnessDatabase.getInstance(application).planDao
        planRepository = PlanRepository(planDao)
        getAllPlans()
    }

    fun getAllPlans() {
        plansList = planRepository.getAllPlans()
    }

    fun addPlan(plan: Plan) {
        viewModelScope.launch {
            planRepository.addPlan(plan)
        }
    }

    fun updatePlan(plan: Plan) {
        viewModelScope.launch {
            planRepository.updatePlan(plan)
        }
    }

    fun deletePlan(plan: Plan) {
        viewModelScope.launch {
            planRepository.deletePlan(plan)
        }
    }

    fun deleteAllPlans() {
        viewModelScope.launch {
            planRepository.deleteAllPlans()
        }
    }

}
package com.calcontrol.app.ui.navigation

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.calcontrol.app.data.database.FitnessDatabase
import com.calcontrol.app.data.model.apimodel.RemoteExercise
import com.calcontrol.app.data.model.apimodel.RemoteIngredient
import com.calcontrol.app.data.model.databasemodel.*
import com.calcontrol.app.data.repository.DayRepository
import com.calcontrol.app.data.repository.FoodRepository
import com.calcontrol.app.data.repository.TrainingRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val dayRepository: DayRepository

    // this is set in NavigationActivity when PlanSelectionActivity sends selected Plan
    var currentPlan = Plan(1, "", 0, 0, 0, "", 0.0, 0.0)

    // this is set in NavigationActivity and based on this DialogFragment fetches appropriate day (recordedDay)
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    var currentDate: String = sdf.format(Date())

    // used for fetching currently selected day
    var recordedDay: LiveData<RecordedDay>

    // for list of SavedMeals in DashboardFragment's Dialog
    var mealsListForConsumption = mutableListOf<SavedMeal>()

    // for list of SavedWorkouts in DashboardFragment's Dialog
    var workoutsListForConsumption = mutableListOf<SavedWorkout>()


    private val foodRepository: FoodRepository

    // for list of SavedMeals in FoodFragment
    lateinit var mealsList: LiveData<List<SavedMeal>>

    // for list of RemoteIngredient in SearchIngredientDialogFragment
    private val _fetchedIngredients = MutableLiveData<List<RemoteIngredient>>()
    val fetchedIngredients: LiveData<List<RemoteIngredient>> = _fetchedIngredients

    // for list of SavedIngredients of a SavedMeal in EditSavedMealDialogFragment
    // value of mealSearchId is changed every time a SavedMeal is selected from the list in EditSavedMealDialogFragment
    // that value is then used to fetch SavedIngredients of a SavedMeal with that id
    var mealSearchId = MutableLiveData<Int>()
    var mealSavedIngredients: LiveData<List<SavedIngredient>>


    private val trainingRepository: TrainingRepository

    // for list of SavedWorkouts in TrainingFragment
    lateinit var workoutsList: LiveData<List<SavedWorkout>>

    // for list of RemoteExercise in SearchExerciseDialogFragment
    private val _fetchedExercises = MutableLiveData<List<RemoteExercise>>()
    val fetchedExercises: LiveData<List<RemoteExercise>> = _fetchedExercises

    // for list of SavedExercises of a SavedWorkout in EditSavedWorkoutDialogFragment
    // value of workoutSearchId is changed every time a SavedWorkout is selected from the list in EditSavedWorkoutDialogFragment
    // that value is then used to fetch SavedExercises of a SavedWorkout with that id
    var workoutSearchId = MutableLiveData<Int>()
    var workoutSavedExercises: LiveData<List<SavedExercise>>

    var currentlySelectedFragmentId: Int = 0

    init {

        val recordedDayDao = FitnessDatabase.getInstance(application).recordedDayDao
        dayRepository = DayRepository(recordedDayDao)
        recordedDay = dayRepository.getRecordedDayFromDateLiveData("16-01-2022", 1)

        val savedMealDao = FitnessDatabase.getInstance(application).savedMealDao
        val savedIngredientDao = FitnessDatabase.getInstance(application).savedIngredientDao
        val consumedMealDao = FitnessDatabase.getInstance(application).consumedMealDao
        foodRepository = FoodRepository(savedIngredientDao, savedMealDao, consumedMealDao)
        // for list of SavedMeals in FoodFragment
        getAllSavedMealsFromPlan(currentPlan.id!!)
        // for list of SavedIngredients of a SavedMeal in EditSavedMealDialogFragment
        mealSearchId.value = 5
        mealSavedIngredients =
            foodRepository.getAllIngredientsFromSavedMealLiveDataTransformations(mealSearchId.value!!)


        val savedWorkoutDAO = FitnessDatabase.getInstance(application).savedWorkoutDao
        val savedExerciseDAO = FitnessDatabase.getInstance(application).savedExerciseDao
        val consumedWorkoutDao = FitnessDatabase.getInstance(application).consumedWorkoutDao
        trainingRepository =
            TrainingRepository(savedExerciseDAO, savedWorkoutDAO, consumedWorkoutDao)
        // for list of SavedWorkouts in TrainingFragment
        getAllSavedWorkoutsFromPlan(currentPlan.id!!)
        // for EditSavedWorkoutDialogFragment
        workoutSearchId.value = 5
        workoutSavedExercises =
            trainingRepository.getAllExercisesFromSavedWorkoutLiveDataTransformations(
                workoutSearchId.value!!
            )

    }

    //region Meals and ingredients

    fun getAllSavedMealsFromPlan(planId: Int) {
        mealsList = foodRepository.getAllSavedMealsFromPlan(planId)
    }

    fun addSavedIngredient(savedIngredient: SavedIngredient) {
        viewModelScope.launch {
            foodRepository.addSavedIngredient(savedIngredient)
        }
    }

    fun updateSavedIngredient(savedIngredient: SavedIngredient) {
        viewModelScope.launch {
            foodRepository.updateSavedIngredient(savedIngredient)
        }
    }

    fun deleteSavedIngredient(savedIngredient: SavedIngredient) {
        viewModelScope.launch {
            foodRepository.deleteSavedIngredient(savedIngredient)
        }
    }

    fun deleteAllSavedIngredients() {
        viewModelScope.launch {
            foodRepository.deleteAllSavedIngredients()
        }
    }

    fun getAllIngredientsFromSavedMealLiveDataTransformations(search_id: Int) {
        viewModelScope.launch {
            mealSearchId.value = search_id
            mealSavedIngredients = Transformations.switchMap(mealSearchId) { search_id ->
                foodRepository.getAllIngredientsFromSavedMealLiveDataTransformations(search_id)
            }
        }
    }

    fun setMealId(search_id: Int) {
        mealSearchId.value = search_id
    }

    fun deleteAllIngredientsFromSavedMeal(meal_id: Int) {
        viewModelScope.launch {
            foodRepository.deleteAllIngredientsFromSavedMeal(meal_id)
        }
    }

    fun addSavedMeal(savedMeal: SavedMeal) {
        viewModelScope.launch {
            foodRepository.addSavedMeal(savedMeal)
        }
    }

    fun updateSavedMeal(savedMeal: SavedMeal) {
        viewModelScope.launch {
            foodRepository.updateSavedMeal(savedMeal)
        }
    }

    fun deleteSavedMeal(savedMeal: SavedMeal) {
        viewModelScope.launch {
            foodRepository.deleteSavedMeal(savedMeal)
        }
    }

    fun deleteAllSavedMeals() {
        viewModelScope.launch {
            foodRepository.deleteAllSavedMeals()
        }
    }

    fun getNetworkIngredients(searchQuery: String) {
        viewModelScope.launch {
            if(searchQuery.isNotEmpty()) {
                try {
                    _fetchedIngredients.value = foodRepository.getNetworkIngredients(searchQuery)
                }
                catch (e: Exception) { e.printStackTrace() }
            }
        }
    }

    //endregion

    //region Workouts and exercises

    fun getAllSavedWorkoutsFromPlan(planId: Int) {
        workoutsList = trainingRepository.getAllSavedWorkoutsFromPlan(planId)
    }

    fun addSavedExercise(savedExercise: SavedExercise) {
        viewModelScope.launch {
            trainingRepository.addSavedExercise(savedExercise)
        }
    }

    fun updateSavedExercise(savedExercise: SavedExercise) {
        viewModelScope.launch {
            trainingRepository.updateSavedExercise(savedExercise)
        }
    }

    fun deleteSavedExercise(savedExercise: SavedExercise) {
        viewModelScope.launch {
            trainingRepository.deleteSavedExercise(savedExercise)
        }
    }

    fun deleteAllSavedExercises() {
        viewModelScope.launch {
            trainingRepository.deleteAllSavedExercises()
        }
    }

    fun getAllExercisesFromSavedWorkoutLiveDataTransformations(search_id: Int) {
        viewModelScope.launch {
            workoutSearchId.value = search_id
            workoutSavedExercises = Transformations.switchMap(workoutSearchId) { search_id ->
                trainingRepository.getAllExercisesFromSavedWorkoutLiveDataTransformations(search_id)
            }
        }
    }

    fun setWorkoutId(search_id: Int) {
        workoutSearchId.value = search_id
    }

    fun deleteAllExercisesFromSavedWorkout(workout_id: Int) {
        viewModelScope.launch {
            trainingRepository.deleteAllExercisesFromSavedWorkout(workout_id)
        }
    }

    fun addSavedWorkout(savedWorkout: SavedWorkout) {
        viewModelScope.launch {
            trainingRepository.addSavedWorkout(savedWorkout)
        }
    }

    fun updateSavedWorkout(savedWorkout: SavedWorkout) {
        viewModelScope.launch {
            trainingRepository.updateSavedWorkout(savedWorkout)
        }
    }

    fun deleteSavedWorkout(savedWorkout: SavedWorkout) {
        viewModelScope.launch {
            trainingRepository.deleteSavedWorkout(savedWorkout)
        }
    }

    fun deleteAllSavedWorkouts() {
        viewModelScope.launch {
            trainingRepository.deleteAllSavedWorkouts()
        }
    }

    fun getNetworkExercises(searchQuery: String) {
        viewModelScope.launch {
            if(searchQuery.isNotEmpty()) {
                try {
                    _fetchedExercises.value = trainingRepository.getNetworkExercises(searchQuery)
                }
                catch (e: Exception) { e.printStackTrace() }
            }
        }
    }

    //endregion

    //region Days and Consumption

    //region Day

    fun addRecordedDay(recordedDay: RecordedDay) {
        viewModelScope.launch {
            dayRepository.addRecordedDay(recordedDay)
        }
    }

    fun updateRecordedDay(recordedDay: RecordedDay) {
        viewModelScope.launch {
            dayRepository.updateRecordedDay(recordedDay)
        }
    }

    fun deleteRecordedDay(recordedDay: RecordedDay) {
        viewModelScope.launch {
            dayRepository.deleteRecordedDay(recordedDay)
        }
    }

    fun deleteAllRecordedDays() {
        viewModelScope.launch {
            dayRepository.deleteAllRecordedDays()
        }
    }

    fun getRecordedDayFromDateLiveData(day_date: String, plan_id: Int) {
        recordedDay = dayRepository.getRecordedDayFromDateLiveData(day_date, plan_id)
    }

    // endregion

    //region Consumption
    fun addConsumedMeal(consumedMeal: ConsumedMeal) {
        viewModelScope.launch {
            foodRepository.addConsumedMeal(consumedMeal)
        }
    }

    fun updateConsumedMeal(consumedMeal: ConsumedMeal) {
        viewModelScope.launch {
            foodRepository.updateConsumedMeal(consumedMeal)
        }
    }

    fun deleteConsumedMeal(consumedMeal: ConsumedMeal) {
        viewModelScope.launch {
            foodRepository.deleteConsumedMeal(consumedMeal)
        }
    }

    fun deleteAllConsumedMeals() {
        viewModelScope.launch {
            foodRepository.deleteAllConsumedMeals()
        }
    }

    fun addConsumedWorkout(consumedWorkout: ConsumedWorkout) {
        viewModelScope.launch {
            trainingRepository.addConsumedWorkout(consumedWorkout)
        }
    }

    fun updateConsumedWorkout(consumedWorkout: ConsumedWorkout) {
        viewModelScope.launch {
            trainingRepository.updateConsumedWorkout(consumedWorkout)
        }
    }

    fun deleteConsumedWorkout(consumedWorkout: ConsumedWorkout) {
        viewModelScope.launch {
            trainingRepository.deleteConsumedWorkout(consumedWorkout)
        }
    }

    fun deleteAllConsumedWorkouts() {
        viewModelScope.launch {
            trainingRepository.deleteAllConsumedWorkouts()
        }
    }

    suspend fun getAllSavedMealsFromPlanNoLiveData(plan_id: Int) {
        mealsListForConsumption = foodRepository.getAllSavedMealsFromPlanNoLiveData(plan_id)
    }

    suspend fun getAllSavedWorkoutsFromPlanNoLiveData(plan_id: Int) {
        workoutsListForConsumption =
            trainingRepository.getAllSavedWorkoutsFromPlanNoLiveData(plan_id)
    }

    fun deleteAllConsumedMealsFromDay(day_id: Int) {
        viewModelScope.launch {
            foodRepository.deleteAllConsumedMealsFromDay(day_id)
        }
    }

    fun deleteAllConsumedWorkoutsFromDay(day_id: Int) {
        viewModelScope.launch {
            trainingRepository.deleteAllConsumedWorkoutsFromDay(day_id)
        }
    }

    //endregion

    suspend fun doesDayWithThisPlanExist(day_date: String, plan_id: Int): Boolean {
        return dayRepository.doesDayWithThisPlanExist(day_date, plan_id)
    }


    //endregion

    fun setPlan(plan: Plan) {
        currentPlan = plan
    }

//    fun setDate(date: String) {
//        currentDate = date
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(daysToChange: Int) {

        // convert to Date from String
        val currentDateDateFormat = sdf.parse(currentDate)
        val calendar = Calendar.getInstance()
        calendar.time = currentDateDateFormat
        calendar.add(Calendar.DATE, daysToChange)
        // convert back to String from Date
        currentDate = sdf.format(calendar.time)

    }

}
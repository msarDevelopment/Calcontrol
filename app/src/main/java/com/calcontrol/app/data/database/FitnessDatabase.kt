package com.calcontrol.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.calcontrol.app.data.model.databasemodel.*
import com.calcontrol.app.data.model.databasemodel.dao.*

@Database(
    entities = [
        SavedMeal::class,
        SavedIngredient::class,
        SavedWorkout::class,
        SavedExercise::class,
        RecordedDay::class,
        ConsumedMeal::class,
        ConsumedWorkout::class,
        Plan::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitnessDatabase : RoomDatabase() {

    abstract val savedMealDao: SavedMealDAO
    abstract val savedIngredientDao: SavedIngredientDAO
    abstract val savedWorkoutDao: SavedWorkoutDAO
    abstract val savedExerciseDao: SavedExerciseDAO

    abstract val recordedDayDao: RecordedDayDAO
    abstract val consumedMealDao: ConsumedMealDAO
    abstract val consumedWorkoutDao: ConsumedWorkoutDAO

    abstract val planDao: PlanDAO

    companion object {
        @Volatile
        private var INSTANCE: FitnessDatabase? = null
        fun getInstance(context: Context): FitnessDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FitnessDatabase::class.java,
                        "fitness_database"
                    ).build()
                }
                return instance
            }
        }

    }

}
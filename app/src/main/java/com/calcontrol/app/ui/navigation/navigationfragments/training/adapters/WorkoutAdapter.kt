package com.calcontrol.app.ui.navigation.navigationfragments.training.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.SavedWorkout
import com.calcontrol.app.ui.navigation.navigationfragments.training.EditSavedWorkoutDialogFragment
import com.calcontrol.app.R

class WorkoutAdapter(
    supportFragmentManager: FragmentManager,
    val deleteSavedWorkout: (SavedWorkout) -> Unit,
    val deleteAllExercisesFromSavedWorkout: (Int) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private var workouts = emptyList<SavedWorkout>()

    // this needs to be passed to adapter in order to get FragmentDialog when item is clicked
    private var supportFragmentManager = supportFragmentManager

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSavedWorkoutName: TextView = itemView.findViewById(R.id.tvSavedParentItemName)
        val tvTotalCaloriesOfWorkout: TextView = itemView.findViewById(R.id.tvTotalCaloriesOfItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal_or_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {

        val currentItem = workouts[position]

        holder.apply {
            tvSavedWorkoutName.text = currentItem.saved_workout_name
            tvTotalCaloriesOfWorkout.text = "${currentItem.total_calories.toString()} kcal"

            itemView.setOnClickListener {
                val dialog = EditSavedWorkoutDialogFragment()
                val bundle = Bundle()
                bundle.putSerializable("workout", currentItem)
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "AddNewWorkoutDialog")
            }
        }

    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    fun setData(meals: List<SavedWorkout>) {
        this.workouts = meals
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {

        val currentItem = workouts[position]

        deleteAllExercisesFromSavedWorkout(currentItem.id!!)
        deleteSavedWorkout(currentItem)
        notifyItemRemoved(position)
    }

}
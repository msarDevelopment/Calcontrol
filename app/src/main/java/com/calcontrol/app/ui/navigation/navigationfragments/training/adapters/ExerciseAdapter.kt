package com.calcontrol.app.ui.navigation.navigationfragments.training.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.SavedExercise
import com.calcontrol.app.R

class ExerciseAdapter(
    val updateSavedExercise: (SavedExercise) -> Unit,
    val deleteSavedExercise: (SavedExercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    var exercises = emptyList<SavedExercise>()

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSavedExerciseName: TextView = itemView.findViewById(R.id.tvSavedChildItemName)
        val npAmount: NumberPicker = itemView.findViewById(R.id.npSavedChildItemAmount)
        val tvMeasurementUnit: TextView =
            itemView.findViewById(R.id.tvSavedChildItemMeasurementUnit)
        val tvSavedExerciseKcal: TextView = itemView.findViewById(R.id.tvSavedChildItemKcal)
        val ivSaveUpdatedExercise: ImageView = itemView.findViewById(R.id.ivSaveUpdatedChildItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient_or_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {

        val currentItem = exercises[position]

        holder.apply {
            tvSavedExerciseName.text = currentItem.saved_exercise_name

            npAmount.minValue = 1
            npAmount.maxValue = 1000
            npAmount.value = currentItem.duration

            npAmount.setOnValueChangedListener { numberPicker, i, i2 ->
                val customCalculatedCalories = i2 * currentItem.calories_per_min
                tvSavedExerciseKcal.text = "${String.format("%.2f", customCalculatedCalories)} kcal"
            }

            tvMeasurementUnit.text = "min"

            // this is only for initial value
            tvSavedExerciseKcal.text =
                "${String.format("%.2f", currentItem.custom_calculated_calories)} kcal"

            ivSaveUpdatedExercise.setOnClickListener {

                val exerciseToUpdate = SavedExercise(
                    currentItem.exr_id,
                    currentItem.saved_exercise_name,
                    currentItem.met,
                    npAmount.value,
                    currentItem.calories_per_min,
                    String.format("%.2f", npAmount.value * currentItem.calories_per_min).toDouble(),
                    currentItem.saved_workout_id,
                    currentItem.source_id
                )

                updateSavedExercise(exerciseToUpdate)

            }

        }

    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun setData(exercises: List<SavedExercise>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {

        val currentItem = exercises[position]

        deleteSavedExercise(currentItem)
        notifyItemRemoved(position)
    }

}
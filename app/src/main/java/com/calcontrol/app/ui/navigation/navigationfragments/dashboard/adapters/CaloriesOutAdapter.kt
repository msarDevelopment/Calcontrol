package com.calcontrol.app.ui.navigation.navigationfragments.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.ConsumedWorkout
import com.calcontrol.app.data.model.databasemodel.RecordedDay
import com.calcontrol.app.data.model.databasemodel.SavedWorkout
import com.calcontrol.app.R


// item_network_result xml will be reused for this recyclerview
class CaloriesOutAdapter(
    val recordedDay: RecordedDay,
    val addSavedWorkoutToConsumedWorkout: (ConsumedWorkout) -> Unit,
    val updateCaloriesOutOfRecordedDay: (RecordedDay) -> Unit
) : RecyclerView.Adapter<CaloriesOutAdapter.CaloriesOutViewHolder>() {

    private var savedWorkouts = emptyList<SavedWorkout>()

    inner class CaloriesOutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNetworkResponseItemName: TextView =
            itemView.findViewById(R.id.tvNetworkResponseItemName)
        val btnAddNetworkResponse: TextView = itemView.findViewById(R.id.btnAddNetworkResponse)
        val tvNetworkResponseKcalPer100: TextView =
            itemView.findViewById(R.id.tvNetworkResponseKcalPer100OrMet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaloriesOutViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_network_result, parent, false)
        return CaloriesOutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaloriesOutViewHolder, position: Int) {

        val currentItem = savedWorkouts[position]

        holder.apply {
            tvNetworkResponseItemName.text = currentItem.saved_workout_name

            tvNetworkResponseKcalPer100.text = "${currentItem.total_calories} kcal"

            btnAddNetworkResponse.setOnClickListener {

                val workoutToConsume = ConsumedWorkout(
                    null,
                    currentItem.saved_workout_name,
                    currentItem.total_calories,
                    recordedDay.day_id!!
                )

                recordedDay.calories_out += String.format("%.2f", currentItem.total_calories)
                    .toDouble()
                addSavedWorkoutToConsumedWorkout(workoutToConsume)
                updateCaloriesOutOfRecordedDay(recordedDay)
            }
        }

    }

    override fun getItemCount(): Int {
        return savedWorkouts.size
    }

    fun setData(savedWorkouts: List<SavedWorkout>) {
        this.savedWorkouts = savedWorkouts
        notifyDataSetChanged()
    }

}
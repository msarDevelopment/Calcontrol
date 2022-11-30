package com.calcontrol.app.ui.navigation.navigationfragments.training.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.apimodel.RemoteExercise
import com.calcontrol.app.data.model.databasemodel.SavedExercise
import com.calcontrol.app.data.model.databasemodel.SavedWorkout
import com.calcontrol.app.R

class NetworkResponseExerciseAdapter(
    val workout: SavedWorkout,
    val addNetworkExerciseToCurrentWorkout: (SavedExercise) -> Unit,
    val planBmr: Double
) : RecyclerView.Adapter<NetworkResponseExerciseAdapter.IngredientRemoteViewHolder>() {

    private var responses = emptyList<RemoteExercise>()

    inner class IngredientRemoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNetworkResponseItemName: TextView =
            itemView.findViewById(R.id.tvNetworkResponseItemName)
        val btnAddNetworkResponse: TextView = itemView.findViewById(R.id.btnAddNetworkResponse)
        val tvNetworkResponseMet: TextView =
            itemView.findViewById(R.id.tvNetworkResponseKcalPer100OrMet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientRemoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_network_result, parent, false)
        return IngredientRemoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientRemoteViewHolder, position: Int) {

        val currentItem = responses[position]

        holder.apply {
            tvNetworkResponseItemName.text = currentItem.exercise_name

            val caloriesPerMin = currentItem.met_value * (planBmr / 24) / 60

            tvNetworkResponseMet.text =
                "${String.format("%.2f", caloriesPerMin * 60).toDouble()} kcal / hour"

            btnAddNetworkResponse.setOnClickListener {

                val exerciseToSave = SavedExercise(
                    null,
                    currentItem.exercise_name,
                    currentItem.met_value,
                    60,
                    caloriesPerMin,
                    caloriesPerMin * 60,
                    workout.id!!,
                    currentItem.id
                )

                addNetworkExerciseToCurrentWorkout(exerciseToSave)
            }
        }

    }

    override fun getItemCount(): Int {
        return responses.size
    }

    fun setData(responses: List<RemoteExercise>) {
        this.responses = responses
        notifyDataSetChanged()
    }

}
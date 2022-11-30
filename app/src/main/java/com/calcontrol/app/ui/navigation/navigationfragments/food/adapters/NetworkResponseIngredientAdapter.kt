package com.calcontrol.app.ui.navigation.navigationfragments.food.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.apimodel.RemoteIngredient
import com.calcontrol.app.data.model.databasemodel.SavedIngredient
import com.calcontrol.app.data.model.databasemodel.SavedMeal
import com.calcontrol.app.R

class NetworkResponseIngredientAdapter(
    val meal: SavedMeal,
    val addNetworkIngredientToCurrentMeal: (SavedIngredient) -> Unit
) : RecyclerView.Adapter<NetworkResponseIngredientAdapter.IngredientRemoteViewHolder>() {

    private var responses = emptyList<RemoteIngredient>()

    inner class IngredientRemoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNetworkResponseItemName: TextView =
            itemView.findViewById(R.id.tvNetworkResponseItemName)
        val btnAddNetworkResponse: TextView = itemView.findViewById(R.id.btnAddNetworkResponse)
        val tvNetworkResponseKcalPer100: TextView =
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
            tvNetworkResponseItemName.text = currentItem.ingredient_name

            tvNetworkResponseKcalPer100.text =
                "${currentItem.energy_kcal} kcal / 100 ${currentItem.measurement_unit}"

            btnAddNetworkResponse.setOnClickListener {
                val ingredientToSave = SavedIngredient(
                    null,
                    currentItem.ingredient_name,
                    currentItem.measurement_amount,
                    currentItem.measurement_unit,
                    currentItem.energy_kcal,
                    meal.id!!,
                    currentItem.measurement_amount,
                    currentItem.energy_kcal,
                    currentItem.id
                )
                addNetworkIngredientToCurrentMeal(ingredientToSave)
            }
        }

    }

    override fun getItemCount(): Int {
        return responses.size
    }

    fun setData(responses: List<RemoteIngredient>) {
        this.responses = responses
        notifyDataSetChanged()
    }

}
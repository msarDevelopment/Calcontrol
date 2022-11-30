package com.calcontrol.app.ui.navigation.navigationfragments.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.ConsumedMeal
import com.calcontrol.app.data.model.databasemodel.RecordedDay
import com.calcontrol.app.data.model.databasemodel.SavedMeal
import com.calcontrol.app.R

// item_network_result xml will be reused for this recyclerview
class CaloriesInAdapter(
    val recordedDay: RecordedDay,
    val addSavedMealToConsumedMeal: (ConsumedMeal) -> Unit,
    val updateCaloriesInOfRecordedDay: (RecordedDay) -> Unit
) : RecyclerView.Adapter<CaloriesInAdapter.CaloriesInViewHolder>() {

    private var savedMeals = emptyList<SavedMeal>()

    inner class CaloriesInViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNetworkResponseItemName: TextView =
            itemView.findViewById(R.id.tvNetworkResponseItemName)
        val btnAddNetworkResponse: TextView = itemView.findViewById(R.id.btnAddNetworkResponse)
        val tvNetworkResponseKcalPer100: TextView =
            itemView.findViewById(R.id.tvNetworkResponseKcalPer100OrMet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaloriesInViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_network_result, parent, false)
        return CaloriesInViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaloriesInViewHolder, position: Int) {

        val currentItem = savedMeals[position]

        holder.apply {
            tvNetworkResponseItemName.text = currentItem.saved_meal_name

            val mealToConsume = ConsumedMeal(
                null,
                currentItem.saved_meal_name,
                currentItem.total_calories,
                recordedDay.day_id!!
            )

            tvNetworkResponseKcalPer100.text = "${currentItem.total_calories} kcal"

            btnAddNetworkResponse.setOnClickListener {
                recordedDay.calories_in += String.format("%.2f", currentItem.total_calories)
                    .toDouble()
                addSavedMealToConsumedMeal(mealToConsume)
                updateCaloriesInOfRecordedDay(recordedDay)
            }
        }

    }

    override fun getItemCount(): Int {
        return savedMeals.size
    }

    fun setData(savedMeals: List<SavedMeal>) {
        this.savedMeals = savedMeals
        notifyDataSetChanged()
    }

}
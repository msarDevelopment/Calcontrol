package com.calcontrol.app.ui.navigation.navigationfragments.food.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.SavedMeal
import com.calcontrol.app.ui.navigation.navigationfragments.food.EditSavedMealDialogFragment
import com.calcontrol.app.R

class MealAdapter(
    supportFragmentManager: FragmentManager,
    val deleteSavedMeal: (SavedMeal) -> Unit,
    val deleteAllIngredientsFromSavedMeal: (Int) -> Unit
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    private var meals = emptyList<SavedMeal>()

    // this needs to be passed to adapter in order to get FragmentDialog when item is clicked
    private var supportFragmentManager = supportFragmentManager

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSavedMealName: TextView = itemView.findViewById(R.id.tvSavedParentItemName)
        val tvTotalCaloriesOfMeal: TextView = itemView.findViewById(R.id.tvTotalCaloriesOfItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal_or_workout, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {

        val currentItem = meals[position]

        holder.apply {
            tvSavedMealName.text = currentItem.saved_meal_name
            tvTotalCaloriesOfMeal.text = "${currentItem.total_calories.toString()} kcal"

            itemView.setOnClickListener {
                val dialog = EditSavedMealDialogFragment()
                val bundle = Bundle()
                bundle.putSerializable("meal", currentItem)
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "AddNewMealDialog")
            }
        }

    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun setData(meals: List<SavedMeal>) {
        this.meals = meals
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {

        val currentItem = meals[position]

        deleteAllIngredientsFromSavedMeal(currentItem.id!!)
        deleteSavedMeal(currentItem)
        notifyItemRemoved(position)
    }

}
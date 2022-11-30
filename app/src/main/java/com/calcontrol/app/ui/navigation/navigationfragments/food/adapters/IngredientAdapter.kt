package com.calcontrol.app.ui.navigation.navigationfragments.food.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.SavedIngredient
import com.calcontrol.app.R

class IngredientAdapter(
    val updateSavedIngredient: (SavedIngredient) -> Unit,
    val deleteSavedIngredient: (SavedIngredient) -> Unit
) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    var ingredients = emptyList<SavedIngredient>()

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSavedIngredientName: TextView = itemView.findViewById(R.id.tvSavedChildItemName)
        val npAmount: NumberPicker = itemView.findViewById(R.id.npSavedChildItemAmount)
        val tvMeasurementUnit: TextView =
            itemView.findViewById(R.id.tvSavedChildItemMeasurementUnit)
        val tvSavedIngredientKcal: TextView = itemView.findViewById(R.id.tvSavedChildItemKcal)
        val ivSaveUpdatedIngredient: ImageView = itemView.findViewById(R.id.ivSaveUpdatedChildItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient_or_exercise, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {

        val currentItem = ingredients[position]

        holder.apply {
            tvSavedIngredientName.text = currentItem.saved_ingredient_name

            npAmount.minValue = 1
            npAmount.maxValue = 1000
            npAmount.value = currentItem.custom_measurement_amount

            val scaleAmountToKcal = currentItem.base_calories_per_100 / 100

            npAmount.setOnValueChangedListener { numberPicker, i, i2 ->
                val customKcal = i2 * scaleAmountToKcal
                tvSavedIngredientKcal.text = "${String.format("%.2f", customKcal)} kcal"
            }

            tvMeasurementUnit.text = currentItem.measurement_unit

            tvSavedIngredientKcal.text =
                "${String.format("%.2f", currentItem.custom_calculated_calories)} kcal"

            ivSaveUpdatedIngredient.setOnClickListener {

                val ingredientToUpdate = SavedIngredient(
                    currentItem.ing_id,
                    currentItem.saved_ingredient_name,
                    currentItem.base_measurement_amount,
                    currentItem.measurement_unit,
                    currentItem.base_calories_per_100,
                    currentItem.saved_meal_id,
                    npAmount.value,
                    String.format("%.2f", npAmount.value * scaleAmountToKcal).toDouble(),
                    currentItem.source_id
                )

                updateSavedIngredient(ingredientToUpdate)

            }

        }

    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setData(ingredients: List<SavedIngredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {

        val currentItem = ingredients[position]

        deleteSavedIngredient(currentItem)
        notifyItemRemoved(position)
    }

}
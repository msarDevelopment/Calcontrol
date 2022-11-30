package com.calcontrol.app.ui.navigation.navigationfragments.food

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.SavedIngredient
import com.calcontrol.app.data.model.databasemodel.SavedMeal
import com.calcontrol.app.ui.navigation.FitnessViewModel
import com.calcontrol.app.ui.navigation.navigationfragments.food.adapters.IngredientAdapter
import com.calcontrol.app.ui.util.SwipeGesture
import com.calcontrol.app.databinding.FragmentEditSavedMealOrWorkoutDialogBinding

class EditSavedMealDialogFragment() : DialogFragment() {

    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var adapter: IngredientAdapter

    private var _binding: FragmentEditSavedMealOrWorkoutDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // to make dialog full screen
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        } else {
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fitnessViewModel = ViewModelProvider(requireActivity())[FitnessViewModel::class.java]

        _binding = FragmentEditSavedMealOrWorkoutDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setAdapter()

        val bundle = arguments
        val meal = bundle!!.getSerializable("meal") as SavedMeal

        binding.etParentItemName.setText(meal.saved_meal_name)

        fitnessViewModel.getAllIngredientsFromSavedMealLiveDataTransformations(meal.id!!)

        fitnessViewModel.mealSavedIngredients.observe(viewLifecycleOwner, Observer {

            adapter.setData(it)

            // to calculate total calories each time update in SavedIngredients happens
            var total_new_calories = 0.0
            for (ing: SavedIngredient in it)
                total_new_calories += ing.custom_calculated_calories

            meal.total_calories = String.format("%.2f", total_new_calories).toDouble()

            // save meal each time one of its ingredients is updated
            meal.saved_meal_name = binding.etParentItemName.text.toString()
            fitnessViewModel.updateSavedMeal(meal)

        })

        binding.btnAddNewChildItem.text = "Add new ingredient"

        binding.btnAddNewChildItem.setOnClickListener {
            val dialog = SearchIngredientDialogFragment()
            val bundle = Bundle()
            bundle.putSerializable("meal", meal)
            dialog.arguments = bundle
            dialog.show(requireActivity().supportFragmentManager, "AddNewIngredientToMealDialog")
        }

        binding.btnSaveParentItem.setOnClickListener {
            if (binding.etParentItemName.text.isNotEmpty()) {
                meal.saved_meal_name = binding.etParentItemName.text.toString()
                fitnessViewModel.updateSavedMeal(meal)
                dismiss()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        val swipeGesture = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                adapter.removeAt(viewHolder.absoluteAdapterPosition)

                super.onSwiped(viewHolder, direction)
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvParentItemChildItemList)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAdapter() {
        adapter = IngredientAdapter(
            { IngredientToUpdate -> fitnessViewModel.updateSavedIngredient(IngredientToUpdate) },
            { IngredientToDelete -> fitnessViewModel.deleteSavedIngredient(IngredientToDelete) }
        )
        binding.rvParentItemChildItemList.adapter = adapter
        binding.rvParentItemChildItemList.layoutManager = LinearLayoutManager(context)
    }

}
package com.calcontrol.app.ui.navigation.navigationfragments.food

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.SavedMeal
import com.calcontrol.app.ui.navigation.FitnessViewModel
import com.calcontrol.app.ui.navigation.navigationfragments.food.adapters.MealAdapter
import com.calcontrol.app.ui.util.SwipeGesture
import com.calcontrol.app.R
import com.calcontrol.app.databinding.FragmentFoodBinding

class FoodFragment : Fragment() {

    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var adapter: MealAdapter

    private var _binding: FragmentFoodBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fitnessViewModel = ViewModelProvider(requireActivity())[FitnessViewModel::class.java]

        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fitnessViewModel.getAllSavedMealsFromPlan(fitnessViewModel.currentPlan.id!!)

        setAdapter()

        fitnessViewModel.mealsList.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        binding.btnAddNewMeal.setOnClickListener {
            newMealNameDialog()
        }

        val swipeGesture = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                adapter.removeAt(viewHolder.absoluteAdapterPosition)

                super.onSwiped(viewHolder, direction)
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvSavedMeals)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAdapter() {
        adapter = MealAdapter(
            requireActivity().supportFragmentManager,
            { MealToDelete -> fitnessViewModel.deleteSavedMeal(MealToDelete) },
            { MealIdOfIngredientsToDelete ->
                fitnessViewModel.deleteAllIngredientsFromSavedMeal(
                    MealIdOfIngredientsToDelete
                )
            }
        )
        binding.rvSavedMeals.adapter = adapter
        binding.rvSavedMeals.layoutManager = LinearLayoutManager(context)
    }

    private fun newMealNameDialog() {

        var newMealName = "New Meal"

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_enter_name, null)
        val name = dialogLayout.findViewById<EditText>(R.id.etNewEntryName)

        with(builder) {
            setTitle("Enter new meal name")
            setPositiveButton("OK") { dialog, which ->
                newMealName = name.text.toString()
                val newMeal = SavedMeal(null, newMealName, 0.0, fitnessViewModel.currentPlan.id!!)
                fitnessViewModel.addSavedMeal(newMeal)
            }
            setNegativeButton("CANCEL") { dialog, which ->

            }
            setView(dialogLayout)
            show()
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}
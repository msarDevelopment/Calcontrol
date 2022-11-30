package com.calcontrol.app.ui.navigation.navigationfragments.dashboard

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.ui.navigation.FitnessViewModel
import com.calcontrol.app.ui.navigation.navigationfragments.dashboard.adapters.CaloriesInAdapter
import com.calcontrol.app.ui.navigation.navigationfragments.dashboard.adapters.CaloriesOutAdapter
import com.calcontrol.app.R
import com.calcontrol.app.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch

class DashboardFragment() : Fragment() {

    private lateinit var fitnessViewModel: FitnessViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fitnessViewModel = ViewModelProvider(requireActivity())[FitnessViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fitnessViewModel.getRecordedDayFromDateLiveData(
            fitnessViewModel.currentDate,
            fitnessViewModel.currentPlan.id!!
        )

        var totalCaloriesOut = 0.0

        val defaultColor = binding.tvCaloricDeficitOrSurplus.textColors

        fitnessViewModel.recordedDay.observe(viewLifecycleOwner, Observer {

            if (it != null) {

                binding.tvCaloriesIn.text = "${String.format("%.2f", it.calories_in)}"

                totalCaloriesOut =
                    fitnessViewModel.currentPlan.bmr_with_activity_factor + it.calories_out

                binding.tvCaloriesOut.text = "${String.format("%.2f", totalCaloriesOut)}"

                val caloricBalance = it.calories_in - totalCaloriesOut
                val deficitOrSurplus = if (caloricBalance > 0) "SURPLUS" else "DEFICIT"
                val color = if (caloricBalance > 0) Color.GREEN else Color.RED

                if (caloricBalance != 0.0) {
                    binding.tvCaloricDeficitOrSurplus.text = "You are ${
                        String.format("%.2f", caloricBalance).toDouble()
                    } kcal in caloric $deficitOrSurplus"
                    binding.tvCaloricDeficitOrSurplus.setTextColor(color)
                } else {
                    binding.tvCaloricDeficitOrSurplus.text =
                        "Perfectly balanced, as all things should be"
                    binding.tvCaloricDeficitOrSurplus.setTextColor(defaultColor)
                }

            }

        })

        binding.btnAddCaloriesIn.setOnClickListener {
            newCaloriesInOutDialog("meal")
        }

        binding.btnAddCaloriesOut.setOnClickListener {
            newCaloriesInOutDialog("workout")
        }

        binding.ivResetCaloriesIn.setOnClickListener {
            fitnessViewModel.recordedDay.value?.calories_in = 0.0
            fitnessViewModel.updateRecordedDay(fitnessViewModel.recordedDay.value!!)
            fitnessViewModel.deleteAllConsumedMealsFromDay(fitnessViewModel.recordedDay.value!!.day_id!!)
        }

        binding.ivResetCaloriesOut.setOnClickListener {
            fitnessViewModel.recordedDay.value?.calories_out = 0.0
            fitnessViewModel.updateRecordedDay(fitnessViewModel.recordedDay.value!!)
            fitnessViewModel.deleteAllConsumedWorkoutsFromDay(fitnessViewModel.recordedDay.value!!.day_id!!)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // fetching with getAllSavedMeals/WorkoutsNoLiveData must be in coroutine because it is doing database operations
    private fun newCaloriesInOutDialog(addTo: String) {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_enter_calories_in_or_out, null)

        val recyclerViewSavedItems = dialogLayout.findViewById<RecyclerView>(R.id.rvSavedItemsList)

        // decide whether to show meals or workouts in Dialog's RecyclerView list
        if (addTo == "meal") {
            val caloriesInAdapter = CaloriesInAdapter(
                fitnessViewModel.recordedDay.value!!,
                { ConsumedMeal -> fitnessViewModel.addConsumedMeal(ConsumedMeal) },
                { RecordedDay -> fitnessViewModel.updateRecordedDay(RecordedDay) }
            )
            lifecycleScope.launch {
                fitnessViewModel.getAllSavedMealsFromPlanNoLiveData(fitnessViewModel.currentPlan.id!!)
                caloriesInAdapter.setData(fitnessViewModel.mealsListForConsumption)
                recyclerViewSavedItems.adapter = caloriesInAdapter
                recyclerViewSavedItems.layoutManager = LinearLayoutManager(context)
            }
        } else {
            val caloriesOutAdapter = CaloriesOutAdapter(
                fitnessViewModel.recordedDay.value!!,
                { ConsumedWorkout -> fitnessViewModel.addConsumedWorkout(ConsumedWorkout) },
                { RecordedDay -> fitnessViewModel.updateRecordedDay(RecordedDay) }
            )
            lifecycleScope.launch {
                fitnessViewModel.getAllSavedWorkoutsFromPlanNoLiveData(fitnessViewModel.currentPlan.id!!)
                caloriesOutAdapter.setData(fitnessViewModel.workoutsListForConsumption)
                recyclerViewSavedItems.adapter = caloriesOutAdapter
                recyclerViewSavedItems.layoutManager = LinearLayoutManager(context)
            }
        }

        with(builder) {
            setTitle("Add new $addTo to day")
            setPositiveButton("OK") { dialog, which -> }
            setView(dialogLayout)
            show()
        }

    }

}
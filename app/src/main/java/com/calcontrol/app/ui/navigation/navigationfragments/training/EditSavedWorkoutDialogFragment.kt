package com.calcontrol.app.ui.navigation.navigationfragments.training

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
import com.calcontrol.app.data.model.databasemodel.SavedExercise
import com.calcontrol.app.data.model.databasemodel.SavedWorkout
import com.calcontrol.app.ui.navigation.FitnessViewModel
import com.calcontrol.app.ui.navigation.navigationfragments.training.adapters.ExerciseAdapter
import com.calcontrol.app.ui.util.SwipeGesture
import com.calcontrol.app.databinding.FragmentEditSavedMealOrWorkoutDialogBinding

class EditSavedWorkoutDialogFragment() : DialogFragment() {

    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var adapter: ExerciseAdapter

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
        val workout = bundle!!.getSerializable("workout") as SavedWorkout

        binding.etParentItemName.setText(workout.saved_workout_name)

        fitnessViewModel.getAllExercisesFromSavedWorkoutLiveDataTransformations(workout.id!!)

        fitnessViewModel.workoutSavedExercises.observe(viewLifecycleOwner, Observer {

            adapter.setData(it)

            // to calculate total calories each time update in SavedWorkouts happens
            var total_new_calories = 0.0
            for (exr: SavedExercise in it)
                total_new_calories += exr.custom_calculated_calories

            workout.total_calories = String.format("%.2f", total_new_calories).toDouble()

            // save workout each time one of its exercises is updated
            workout.saved_workout_name = binding.etParentItemName.text.toString()
            fitnessViewModel.updateSavedWorkout(workout)

        })

        binding.btnAddNewChildItem.text = "Add new exercise"

        binding.btnAddNewChildItem.setOnClickListener {
            val dialog = SearchExerciseDialogFragment()
            val bundle = Bundle()
            bundle.putSerializable("workout", workout)
            dialog.arguments = bundle
            dialog.show(requireActivity().supportFragmentManager, "AddNewExerciseToWorkoutDialog")
        }

        binding.btnSaveParentItem.setOnClickListener {
            if (binding.etParentItemName.text.isNotEmpty()) {
                workout.saved_workout_name = binding.etParentItemName.text.toString()
                fitnessViewModel.updateSavedWorkout(workout)
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
        adapter = ExerciseAdapter(
            { ExerciseToUpdate -> fitnessViewModel.updateSavedExercise(ExerciseToUpdate) },
            { ExerciseToDelete -> fitnessViewModel.deleteSavedExercise(ExerciseToDelete) }
        )
        binding.rvParentItemChildItemList.adapter = adapter
        binding.rvParentItemChildItemList.layoutManager = LinearLayoutManager(context)
    }

}
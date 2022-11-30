package com.calcontrol.app.ui.navigation.navigationfragments.training

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
import com.calcontrol.app.data.model.databasemodel.SavedWorkout
import com.calcontrol.app.ui.navigation.FitnessViewModel
import com.calcontrol.app.ui.navigation.navigationfragments.training.adapters.WorkoutAdapter
import com.calcontrol.app.ui.util.SwipeGesture
import com.calcontrol.app.R
import com.calcontrol.app.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var adapter: WorkoutAdapter

    private var _binding: FragmentTrainingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fitnessViewModel = ViewModelProvider(requireActivity())[FitnessViewModel::class.java]

        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fitnessViewModel.getAllSavedWorkoutsFromPlan(fitnessViewModel.currentPlan.id!!)

        setAdapter()

        fitnessViewModel.workoutsList.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        binding.btnAddNewWorkout.setOnClickListener {
            newWorkoutNameDialog()
        }

        val swipeGesture = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                adapter.removeAt(viewHolder.absoluteAdapterPosition)

                super.onSwiped(viewHolder, direction)
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvSavedWorkouts)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAdapter() {
        adapter = WorkoutAdapter(
            requireActivity().supportFragmentManager,
            { WorkoutToDelete -> fitnessViewModel.deleteSavedWorkout(WorkoutToDelete) },
            { WorkoutIdOfExercisesToDelete ->
                fitnessViewModel.deleteAllExercisesFromSavedWorkout(
                    WorkoutIdOfExercisesToDelete
                )
            }
        )
        binding.rvSavedWorkouts.adapter = adapter
        binding.rvSavedWorkouts.layoutManager = LinearLayoutManager(context)
    }

    private fun newWorkoutNameDialog() {

        var newWorkoutName = "New Workout"

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_enter_name, null)
        val name = dialogLayout.findViewById<EditText>(R.id.etNewEntryName)

        with(builder) {
            setTitle("Enter new workout name")
            setPositiveButton("OK") { dialog, which ->

                newWorkoutName = name.text.toString()
                val newWorkout =
                    SavedWorkout(null, newWorkoutName, 0.0, fitnessViewModel.currentPlan.id!!)
                fitnessViewModel.addSavedWorkout(newWorkout)

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
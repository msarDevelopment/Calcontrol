package com.calcontrol.app.ui.navigation.navigationfragments.food

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.calcontrol.app.data.model.databasemodel.SavedMeal
import com.calcontrol.app.ui.navigation.FitnessViewModel
import com.calcontrol.app.ui.navigation.navigationfragments.food.adapters.NetworkResponseIngredientAdapter
import com.calcontrol.app.databinding.FragmentSearchIngredientOrExerciseDialogBinding

class SearchIngredientDialogFragment() : DialogFragment() {

    private lateinit var meal: SavedMeal
    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var adapter: NetworkResponseIngredientAdapter

    private var _binding: FragmentSearchIngredientOrExerciseDialogBinding? = null

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

        _binding =
            FragmentSearchIngredientOrExerciseDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bundle = arguments
        meal = bundle!!.getSerializable("meal") as SavedMeal

        setAdapter()

        fitnessViewModel.fetchedIngredients.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        binding.btnSearchChildItemSearch.setOnClickListener {
            if (binding.etSearchChildItem.text != null) {
                fitnessViewModel.getNetworkIngredients(binding.etSearchChildItem.text.toString())
            }
        }

        binding.btnEnd.setOnClickListener {
            dismiss()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAdapter() {
        adapter = NetworkResponseIngredientAdapter(
            meal,
            { SavedIngredient -> fitnessViewModel.addSavedIngredient(SavedIngredient) }
        )
        binding.rvSearchChildItemList.adapter = adapter
        binding.rvSearchChildItemList.layoutManager = LinearLayoutManager(context)
    }

}
package com.calcontrol.app.ui.plan

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.calcontrol.app.data.model.databasemodel.Plan
import com.calcontrol.app.ui.plan.adapters.PlanAdapter
import com.calcontrol.app.R
import com.calcontrol.app.databinding.ActivityPlanSelectionBinding

class PlanSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanSelectionBinding

    private lateinit var planSelectionViewModel: PlanSelectionViewModel

    private lateinit var adapter: PlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlanSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        planSelectionViewModel = ViewModelProvider(this).get(PlanSelectionViewModel::class.java)

        setAdapter()

        planSelectionViewModel.plansList.observe(this, Observer {
            adapter.setData(it)
        })

        binding.btnAddNewPlan.setOnClickListener {
            newPlanDialog()
        }

    }

    private fun setAdapter() {
        adapter = PlanAdapter()
        binding.rvPlanList.adapter = adapter
        binding.rvPlanList.layoutManager = LinearLayoutManager(this)
    }

    private fun newPlanDialog() {

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_enter_plan, null)

        val etPlanName = dialogLayout.findViewById<EditText>(R.id.etNewPlanName)
        val npPlanWeight = dialogLayout.findViewById<NumberPicker>(R.id.npNewPlanWeight)
        val npPlanHeight = dialogLayout.findViewById<NumberPicker>(R.id.npNewPlanHeight)
        val npPlanAge = dialogLayout.findViewById<NumberPicker>(R.id.npNewPlanAge)
        val rdPlanGender = dialogLayout.findViewById<RadioGroup>(R.id.rdgGender)

        npPlanWeight.minValue = 1
        npPlanWeight.maxValue = 150
        npPlanWeight.value = 75
        npPlanHeight.minValue = 140
        npPlanHeight.maxValue = 230
        npPlanHeight.value = 180
        npPlanAge.minValue = 1
        npPlanAge.maxValue = 120
        npPlanAge.value = 30

        with(builder) {
            setTitle("Enter new plan name")
            setPositiveButton("CREATE") { dialog, which ->

                val newPlanName = if (etPlanName.text.toString().isNotEmpty()) etPlanName.text.toString() else "Plan name"
                val newPlanWeight = npPlanWeight.value
                val newPlanHeight = npPlanHeight.value
                val newPlanAge = npPlanAge.value
                val selectedOption: Int = rdPlanGender.checkedRadioButtonId
                val radioButton = dialogLayout.findViewById<RadioButton>(selectedOption)

                val bmrGenderConstant = if (radioButton.text == "Male") 5 else -161
                val bmr =
                    10 * newPlanWeight + 6.25 * newPlanHeight - 5 * newPlanAge + bmrGenderConstant
                val sedentaryActivityCoefficient = 1.2
                val bmrWithActivityFactor =
                    String.format("%.2f", bmr * sedentaryActivityCoefficient).toDouble()

                val newPlan = Plan(
                    null,
                    newPlanName,
                    newPlanWeight,
                    newPlanHeight,
                    newPlanAge,
                    radioButton.text as String,
                    bmr,
                    bmrWithActivityFactor
                )

                planSelectionViewModel.addPlan(newPlan)

            }
            setNegativeButton("CANCEL") { dialog, which ->

            }
            setView(dialogLayout)
            show()
        }

    }

}
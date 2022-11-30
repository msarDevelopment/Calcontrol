package com.calcontrol.app.ui.navigation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.calcontrol.app.data.model.databasemodel.Plan
import com.calcontrol.app.data.model.databasemodel.RecordedDay
import com.calcontrol.app.ui.navigation.navigationfragments.dashboard.DashboardFragment
import com.calcontrol.app.ui.navigation.navigationfragments.food.FoodFragment
import com.calcontrol.app.ui.navigation.navigationfragments.training.TrainingFragment
import com.calcontrol.app.R
import com.calcontrol.app.databinding.ActivityNavigationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    private lateinit var fitnessViewModel: FitnessViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fitnessViewModel = ViewModelProvider(this)[FitnessViewModel::class.java]

        val plan = intent.getSerializableExtra("plan") as Plan
        fitnessViewModel.setPlan(plan)

        setSupportActionBar(binding.toolbar)

        // this is used to recover appropriate Fragment upon configuration change
        // if id was one of other two Fragments, set Fragment with that id
        // if not, default to DashboardFragment (also covers setting DashboardFragment on first launch)
        if(fitnessViewModel.currentlySelectedFragmentId == R.id.bottom_nav_menu_item_food ||
            fitnessViewModel.currentlySelectedFragmentId == R.id.bottom_nav_menu_item_training) {
            when(fitnessViewModel.currentlySelectedFragmentId) {
                R.id.bottom_nav_menu_item_food -> {
                    setFragment(FoodFragment())
                }
                R.id.bottom_nav_menu_item_training -> {
                    setFragment(TrainingFragment())
                }
            }
        }
        else {
            lifecycleScope.launch {
                checkIfDayExistsInPlan()
            }
        }

        binding.tvDate.text = fitnessViewModel.currentDate

        binding.ivArrowLeft.setOnClickListener {
            fitnessViewModel.setDate(-1)
            binding.tvDate.text = fitnessViewModel.currentDate

            lifecycleScope.launch {
                checkIfDayExistsInPlan()
            }
        }

        binding.ivArrowRight.setOnClickListener {
            fitnessViewModel.setDate(1)
            binding.tvDate.text = fitnessViewModel.currentDate

            lifecycleScope.launch {
                checkIfDayExistsInPlan()
            }
        }

        val navView: BottomNavigationView = binding.bottomNavMenu

        navView.setOnItemSelectedListener {
            fitnessViewModel.currentlySelectedFragmentId = it.itemId
            when (it.itemId) {
                R.id.bottom_nav_menu_item_home -> setFragment(DashboardFragment())
                R.id.bottom_nav_menu_item_food -> setFragment(FoodFragment())
                R.id.bottom_nav_menu_item_training -> setFragment(TrainingFragment())
            }
            true
        }
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    private suspend fun checkIfDayExistsInPlan() {
        val exists = fitnessViewModel.doesDayWithThisPlanExist(
            fitnessViewModel.currentDate,
            fitnessViewModel.currentPlan.id!!
        )
        if (exists) {
            fitnessViewModel.getRecordedDayFromDateLiveData(
                fitnessViewModel.currentDate,
                fitnessViewModel.currentPlan.id!!
            )
        } else {
            val day = RecordedDay(null, fitnessViewModel.currentDate, fitnessViewModel.currentPlan.id!!, 0.0, 0.0)
            fitnessViewModel.addRecordedDay(day)
        }
        setFragment(DashboardFragment())
        fitnessViewModel.currentlySelectedFragmentId = R.id.bottom_nav_menu_item_home
    }

}
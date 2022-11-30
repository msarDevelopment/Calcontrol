package com.calcontrol.app.ui.plan.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcontrol.app.data.model.databasemodel.Plan
import com.calcontrol.app.ui.navigation.NavigationActivity
import com.calcontrol.app.ui.plan.PlanSelectionActivity
import com.calcontrol.app.R

class PlanAdapter() : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    private var plans = emptyList<Plan>()

    private lateinit var context: Context

    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlanName: TextView = itemView.findViewById(R.id.tvPlanName)
        val tvPlanWeight: TextView = itemView.findViewById(R.id.tvPlanWeight)
        val tvPlanHeight: TextView = itemView.findViewById(R.id.tvPlanHeight)
        val tvPlanAge: TextView = itemView.findViewById(R.id.tvPlanAge)
        val tvPlanGender: TextView = itemView.findViewById(R.id.tvPlanGender)
        val tvPlanBasicCaloricNeed: TextView = itemView.findViewById(R.id.tvPlanBaseCaloricNeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {

        val currentItem = plans[position]

        holder.apply {
            tvPlanName.text = currentItem.plan_name
            tvPlanWeight.text = "Weight: ${currentItem.weight.toString()}"
            tvPlanHeight.text = "Height: ${currentItem.height.toString()}"
            tvPlanAge.text = "Age: ${currentItem.age.toString()}"
            tvPlanGender.text = "Gender: ${currentItem.gender.toString()}"
            tvPlanBasicCaloricNeed.text =
                "Basic caloric need: ${currentItem.bmr_with_activity_factor.toString()}"

            itemView.setOnClickListener {
                val intent = Intent(context, NavigationActivity::class.java)
                intent.putExtra("plan", currentItem)
                context.startActivity(intent)
                (context as PlanSelectionActivity).finish()
            }
        }

    }

    override fun getItemCount(): Int {
        return plans.size
    }

    fun setData(plans: List<Plan>) {
        this.plans = plans
        notifyDataSetChanged()
    }

}
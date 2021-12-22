package com.joytekmotion.yemilicious.ui.seller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.joytekmotion.yemilicious.R
import com.joytekmotion.yemilicious.data.FoodViewModel
import com.joytekmotion.yemilicious.helpers.alertBox
import com.joytekmotion.yemilicious.models.Food
import kotlinx.android.synthetic.main.fragment_home.*

const val FOOD = "food"

class SellerHomeFragment : Fragment(), FoodRecyclerViewAdapter.OnFoodClickListener {

    private val foodVm: FoodViewModel by viewModels()
    private val mAdapter by lazy { FoodRecyclerViewAdapter(this.context, this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (foodVm.foods.value == null) {
            foodVm.getFoods()
        }
        foodVm.foods.observe(requireActivity(), {
            if (it.isNullOrEmpty())
                txtNoFood.visibility = View.VISIBLE
            mAdapter.submitList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodVm.foodsErrors.observe(requireActivity(), {
            alertBox(foodsContainer, it, Snackbar.LENGTH_LONG)
        })

        foodVm.foodDeleteSuccess.observe(requireActivity(), {
            alertBox(foodsContainer, it, Snackbar.LENGTH_LONG)
        })

        foodVm.foodDeleteError.observe(requireActivity(), {
            alertBox(foodsContainer, it, Snackbar.LENGTH_LONG)
        })

        rvFoodList.layoutManager = GridLayoutManager(requireContext(), 2)
        rvFoodList.adapter = mAdapter
    }

    override fun onEditFood(food: Food) {
        val intent = Intent(requireContext(), AddFoodActivity::class.java)
        intent.putExtra(FOOD, food)
        startActivity(intent)
    }

    override fun onDeleteFood(food: Food) {
        foodVm.deleteFood(food)
    }
}
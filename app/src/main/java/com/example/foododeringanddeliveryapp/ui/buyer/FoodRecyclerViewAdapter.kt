package com.example.foododeringanddeliveryapp.ui.buyer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.models.Food
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.buyer_food_list.view.*
import kotlin.math.roundToInt

class FoodViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(food: Food, context: Context?, listener: FoodRecyclerViewAdapter.OnFoodClickListener) {
        containerView.txtFoodName.text = food.name
        containerView.txtFoodPrice.text =
            context?.getString(R.string.food_price_naira, food.price.roundToInt().toString())

        containerView.btnOrder.setOnClickListener {
            listener.onOrderFood(food)
        }

        containerView.lytSelectFood.setOnClickListener {
            listener.onSelectFood(food)
        }
    }
}

//private const val TAG = "FoodRecyclerViewAdapt"
class FoodRecyclerViewAdapter(
    private val context: Context?,
    private val listener: OnFoodClickListener
) : RecyclerView.Adapter<FoodViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldFood: Food, newFood: Food): Boolean {
            return oldFood.id == newFood.id
        }

        override fun areContentsTheSame(oldFood: Food, newFood: Food): Boolean {
            return oldFood == newFood

        }
    }

    private val mDiffer = AsyncListDiffer(this, differCallback)

    interface OnFoodClickListener {
        fun onOrderFood(food: Food)
        fun onSelectFood(food: Food)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.buyer_food_list, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = mDiffer.currentList[position]
        holder.bind(food, context, listener)

        val pathReference = Firebase.storage.reference
            .child(food.image!!)
        pathReference.downloadUrl.addOnSuccessListener {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.default_food)
                .error(R.drawable.default_food)
                .into(holder.containerView.ivFoodImage)
        }

    }

    override fun getItemCount(): Int {
//        return if(!foods.isNullOrEmpty()) foods!!.size else 0
        return mDiffer.currentList.size
    }

//    fun updateList(newFoods: ArrayList<Food>?) {
//        if (newFoods != foods) {
//            val numItems = itemCount
//            foods = newFoods
//            if (newFoods != null) notifyDataSetChanged()
//            else notifyItemRangeRemoved(0, numItems)
//        }
//    }

    fun submitList(data: ArrayList<Food>?) {
        mDiffer.submitList(data)
    }
}
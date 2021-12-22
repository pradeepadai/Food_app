package com.example.foododeringanddeliveryapp.ui.seller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.models.Food
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.seller_food_list.view.*
import kotlin.math.roundToInt

class FoodViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(food: Food, context: Context?, listener: FoodRecyclerViewAdapter.OnFoodClickListener) {
        containerView.txtFoodName.text = food.name
        containerView.txtFoodPrice.text =
            context?.getString(R.string.food_price_naira, food.price.roundToInt().toString())

        containerView.ibEditFood.setOnClickListener {
            listener.onEditFood(food)
        }

        containerView.ibDeleteFood.setOnClickListener {
            listener.onDeleteFood(food)
        }
    }
}

//private const val TAG = "FoodRecyclerViewAdapt"
class FoodRecyclerViewAdapter(
    private val context: Context?,
    private val listener: OnFoodClickListener
) : RecyclerView.Adapter<FoodViewHolder>() {
    interface OnFoodClickListener {
        fun onEditFood(food: Food)
        fun onDeleteFood(food: Food)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldFood: Food, newFood: Food): Boolean {
            return oldFood.id == newFood.id
        }

        override fun areContentsTheSame(oldFood: Food, newFood: Food): Boolean {
            return oldFood == newFood

        }
    }

    private val mDiffer = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.seller_food_list, parent, false)
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
        return mDiffer.currentList.size
    }

    fun submitList(data: ArrayList<Food>?) {
        mDiffer.submitList(data)
    }
}
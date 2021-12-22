package com.joytekmotion.yemilicious.ui.buyer

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.joytekmotion.yemilicious.R
import com.joytekmotion.yemilicious.models.Order
import com.joytekmotion.yemilicious.models.OrdersContract
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.buyers_order_item.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class BuyerOrderViewModel(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(
        order: Order,
        context: Context?,
        listener: BuyersOrdersRecyclerViewAdapter.OnBuyerOrderClickListener
    ) {
        containerView.tvOrderListFoodName.text = order.food.name
        containerView.tvOrderListPrice.text = context?.getString(
            R.string.food_price_naira,
            order.food.price.roundToInt().toString()
        )
        containerView.tvOrderListShopName.text =
            order.seller?.shop?.name?.toUpperCase(Locale.getDefault())
        containerView.tvOrderListAddress.text = order.seller?.shop?.address
        containerView.tvOrderListStatus.text = order.status.capitalize(Locale.getDefault())

        when (order.status) {
            OrdersContract.Responses.PENDING -> {
                containerView.btnCancelOrder.visibility = View.VISIBLE
                containerView.btnOrderDelivered.visibility = View.GONE
                containerView.tvOrderListStatus.setTextColor(
                        ContextCompat.getColor(
                                context!!,
                                R.color.yellow
                        )
                )
            }
            OrdersContract.Responses.PROCESSING -> {
                containerView.btnCancelOrder.visibility = View.GONE
                containerView.btnOrderDelivered.visibility = View.VISIBLE
                containerView.tvOrderListStatus.setTextColor(
                        ContextCompat.getColor(
                                context!!,
                                R.color.secondary_text
                        )
                )
            }
            OrdersContract.Responses.DELIVERED -> {
                containerView.btnCancelOrder.visibility = View.GONE
                containerView.btnOrderDelivered.visibility = View.GONE
                containerView.tvOrderListStatus.setTextColor(
                        ContextCompat.getColor(
                                context!!,
                                R.color.green
                        )
                )
            }
            else -> {
                containerView.btnCancelOrder.visibility = View.GONE
                containerView.btnOrderDelivered.visibility = View.GONE
                containerView.tvOrderListStatus.setTextColor(
                        ContextCompat.getColor(
                                context!!,
                                R.color.red
                        )
                )
            }
        }

        containerView.btnCancelOrder.setOnClickListener {
            listener.onCancelClick(order)
        }

        containerView.btnOrderDelivered.setOnClickListener {
            listener.onDeliveredClick(order)
        }
    }
}

class BuyersOrdersRecyclerViewAdapter(
    private val context: Context?,
    private val listener: OnBuyerOrderClickListener
) : RecyclerView.Adapter<BuyerOrderViewModel>() {

    private val differCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldOrder: Order, newOrder: Order): Boolean {
            return oldOrder.id == oldOrder.id
        }

        override fun areContentsTheSame(oldOrder: Order, newOrder: Order): Boolean {
            return oldOrder == newOrder

        }
    }

    private val mDiffer = AsyncListDiffer(this, differCallback)

    interface OnBuyerOrderClickListener {
        fun onCancelClick(order: Order)
        fun onDeliveredClick(order: Order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyerOrderViewModel {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.buyers_order_item, parent, false)
        return BuyerOrderViewModel(view)
    }

    override fun onBindViewHolder(holder: BuyerOrderViewModel, position: Int) {
//        val orders = orders
        val order = mDiffer.currentList[position]
        holder.bind(order, context, listener)
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(data: ArrayList<Order>) {
        mDiffer.submitList(data)
    }

    //    fun updateList(newOrders: ArrayList<Order>?) {
    //        if (newOrders != orders) {
    //            val numItems = itemCount
    //            orders = newOrders
    //            if (newOrders != null) notifyDataSetChanged()
    //            else notifyItemRangeRemoved(0, numItems)
    //        }
    //    }
}
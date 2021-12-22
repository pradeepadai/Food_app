package com.joytekmotion.yemilicious.ui.seller

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
import kotlinx.android.synthetic.main.seller_orders_list.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class SellerOrderViewModel(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(
        order: Order,
        context: Context?,
        listener: SellersOrdersRecyclerViewAdapter.OnSellerOrderClickListener
    ) {
        containerView.txtOrderFoodName.text = order.food.name
        containerView.txtOrderCustomerName.text = context?.getString(
            R.string.order_customer_name,
            order.buyer.name
        )
        containerView.txtOrderFoodPrice.text = context?.getString(
            R.string.food_price_naira,
            order.food.price.roundToInt().toString()
        )
        containerView.txtOrderCustomerAddress.text = context?.getString(
            R.string.order_customer_address,
            order.buyer.address
        )
        containerView.txtOrderCustomerPhone.text = context?.getString(
            R.string.order_customer_phone,
            order.buyer.phone
        )
        val dateFormat = android.text.format.DateFormat.getDateFormat(context)
        containerView.txtOrderDate.text = dateFormat.format(order.timestamp?.toDate()!!)
        containerView.txtOrderStatus.text = order.status.capitalize(Locale.getDefault())

        when (order.status) {
            OrdersContract.Responses.PENDING -> {
                containerView.btnOrderAccept.visibility = View.VISIBLE
                containerView.btnOrderReject.visibility = View.VISIBLE
                containerView.txtOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.yellow
                    )
                )
            }
            OrdersContract.Responses.PROCESSING -> {
                containerView.btnOrderAccept.visibility = View.GONE
                containerView.btnOrderReject.visibility = View.GONE
            }
            OrdersContract.Responses.DELIVERED -> {
                containerView.btnOrderAccept.visibility = View.GONE
                containerView.btnOrderReject.visibility = View.GONE
                containerView.txtOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.green
                    )
                )
            }
            else -> {
                containerView.btnOrderAccept.visibility = View.GONE
                containerView.btnOrderReject.visibility = View.GONE
                containerView.txtOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.red
                    )
                )
            }
        }

        containerView.btnOrderAccept.setOnClickListener {
            listener.onAcceptClick(order)
        }

        containerView.btnOrderReject.setOnClickListener {
            listener.onRejectClick(order)
        }
    }
}

class SellersOrdersRecyclerViewAdapter(
    private val context: Context?,
    private val listener: OnSellerOrderClickListener
) : RecyclerView.Adapter<SellerOrderViewModel>() {
    interface OnSellerOrderClickListener {
        fun onRejectClick(order: Order)
        fun onAcceptClick(order: Order)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldOrder: Order, newOrder: Order): Boolean {
            return oldOrder.status == newOrder.status
        }

        override fun areContentsTheSame(oldOrder: Order, newOrder: Order): Boolean {
            return oldOrder == newOrder

        }
    }

    private val mDiffer = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerOrderViewModel {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.seller_orders_list, parent, false)
        return SellerOrderViewModel(view)
    }

    override fun onBindViewHolder(holder: SellerOrderViewModel, position: Int) {
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
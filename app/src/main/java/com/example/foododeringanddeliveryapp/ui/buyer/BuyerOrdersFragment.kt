package com.joytekmotion.yemilicious.ui.buyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.joytekmotion.yemilicious.R
import com.joytekmotion.yemilicious.data.LoginViewModel
import com.joytekmotion.yemilicious.data.OrderViewModel
import com.joytekmotion.yemilicious.helpers.alertBox
import com.joytekmotion.yemilicious.models.Order
import com.joytekmotion.yemilicious.models.OrdersContract
import kotlinx.android.synthetic.main.fragment_buyer_orders_list.*

/**
 * A fragment representing a list of Items.
 */
class BuyerOrdersFragment : Fragment(), BuyersOrdersRecyclerViewAdapter.OnBuyerOrderClickListener {
    private val loginVm: LoginViewModel by viewModels()
    private val buyerOrderVm: OrderViewModel by viewModels()
    private lateinit var buyerUid: String
    private val mAdapter by lazy {
        BuyersOrdersRecyclerViewAdapter(
                this.context, this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginVm.currentUser.observe(this, {
            buyerOrderVm.getBuyerOrders(it.uid)
            buyerUid = it.uid
        })

        buyerOrderVm.buyerOrders.observe(requireActivity(), {
            mAdapter.submitList(it)
        })

        buyerOrderVm.cancelSuccess.observe(requireActivity(), {
            alertBox(rvOrdersList, it, Snackbar.LENGTH_LONG)
        })

        buyerOrderVm.orderStatusUpdate.observe(requireActivity(), {
            buyerOrderVm.getBuyerOrders(buyerUid)
            alertBox(rvOrdersList, it, Snackbar.LENGTH_LONG)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_buyer_orders_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvOrdersList.layoutManager = LinearLayoutManager(requireContext())
        rvOrdersList.adapter = mAdapter

    }

    override fun onCancelClick(order: Order) {
        buyerOrderVm.removeOrder(order)
    }

    override fun onDeliveredClick(order: Order) {
        buyerOrderVm.updateOrderStatus(order, OrdersContract.Responses.DELIVERED)
    }
}
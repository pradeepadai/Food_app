package com.joytekmotion.yemilicious.ui.seller

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
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment(), SellersOrdersRecyclerViewAdapter.OnSellerOrderClickListener {
    private val loginVm: LoginViewModel by viewModels()
    private val orderVm: OrderViewModel by viewModels()
    private lateinit var sellerUid: String
    private val mAdapter by lazy {
        SellersOrdersRecyclerViewAdapter(
                this.context, this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginVm.currentUser.observe(this, {
            orderVm.getSellerOrders(it.uid)
            sellerUid = it.uid
        })

        orderVm.sellerOrders.observe(requireActivity(), {
            if (it.isNullOrEmpty())
                txtNoOrderAlert.visibility = View.VISIBLE
            mAdapter.submitList(it)
        })

        orderVm.orderStatusUpdate.observe(requireActivity(), {
            alertBox(lytSellOrder, it, Snackbar.LENGTH_LONG)
            orderVm.getSellerOrders(sellerUid)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSellOrdersList.layoutManager = LinearLayoutManager(requireContext())
        rvSellOrdersList.adapter = mAdapter
    }

    override fun onRejectClick(order: Order) {
        orderVm.updateOrderStatus(order, OrdersContract.Responses.REJECTED)
    }

    override fun onAcceptClick(order: Order) {
        orderVm.updateOrderStatus(order, OrdersContract.Responses.PROCESSING)
    }
}
package com.example.foododeringanddeliveryapp.ui.buyer

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.data.BuyerViewModel
import com.example.foododeringanddeliveryapp.data.LoginViewModel
import com.example.foododeringanddeliveryapp.data.OrderViewModel
import com.example.foododeringanddeliveryapp.data.UserViewModel
import com.example.foododeringanddeliveryapp.helpers.alertBox
import com.example.foododeringanddeliveryapp.models.*
import kotlinx.android.synthetic.main.fragment_buy_foods.*
import kotlinx.android.synthetic.main.fragment_display_shops.*
import kotlinx.android.synthetic.main.fragment_display_shops.rvDisplayFoods
import kotlinx.android.synthetic.main.order_summary.view.*
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
private const val TAG = "BuyFoodsFragment"

//class UnbuggyLinearLayoutManager(context: Context): LinearLayoutManager(context) {
//    override fun supportsPredictiveItemAnimations(): Boolean {
//        return super.supportsPredictiveItemAnimations()
//    }
//}
class BuyFoodsFragment : Fragment(), FoodRecyclerViewAdapter.OnFoodClickListener {

    private val buyerVm: BuyerViewModel by viewModels()
    private val sellerVm: UserViewModel by viewModels()
    private val orderVm: OrderViewModel by viewModels()
    private val loginVm: LoginViewModel by viewModels()
    private lateinit var seller: User
    private lateinit var buyer: User
    private var orderDialog: AlertDialog? = null
    private val mAdapter by lazy {
        FoodRecyclerViewAdapter(
            this.context,
            this
        )
    }
    private val args: BuyFoodsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve Sellers
        if (args.sellerUid != null) {
            val sellerUid: String? = args.sellerUid
            sellerVm.getUserProfile(sellerUid!!)
            buyerVm.getSellerFoods(sellerUid)
        }

        orderVm.orderSuccess.observe(requireActivity(), {
            alertBox(lytDisplayFoods, it, Snackbar.LENGTH_LONG)
        })

        orderVm.orderError.observe(requireActivity(), {
            alertBox(lytDisplayFoods, it, Snackbar.LENGTH_LONG)
        })

        loginVm.currentUserInfo.observe(this, {
            buyer = it
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_foods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buyerVm.sellersFoods.observe(requireActivity(), {
            if (it.isNullOrEmpty())
                tvNoFoods.visibility = View.VISIBLE
            else {
                mAdapter.submitList(it)
            }
        })

        rvDisplayFoods.layoutManager = LinearLayoutManager(requireContext())
        rvDisplayFoods.adapter = mAdapter
    }

    override fun onOrderFood(food: Food) {
        val order = Order(food, buyer, food.seller, OrdersContract.Responses.PENDING)
//        showOrderSummary(order)
        orderVm.makeOrder(order)

    }

    override fun onSelectFood(food: Food) {
//        TODO("Not yet implemented")
    }

    private fun showOrderSummary(order: Order) {
        val orderView = layoutInflater.inflate(R.layout.order_summary, null)
        orderView.tvOrderFoodName.text = order.food.name
        orderView.tvOrderPrice.text =
            getString(R.string.food_price_naira, order.food.price.roundToInt().toString())
        orderView.tvShopAddress.text = order.seller?.shop?.address

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.order_summary))
        builder.setIcon(R.drawable.cooking_pot)
        builder.setPositiveButton(getString(R.string.confirm_order)) { _, _ ->
            orderVm.makeOrder(order)
        }
        builder.setNegativeButton(getString(R.string.cancel), null)


        orderDialog = builder.setView(orderView).create()
        orderDialog?.setCanceledOnTouchOutside(true)

        orderDialog?.show()
    }

    override fun onStop() {
        super.onStop()
        if (orderDialog?.isShowing == true) {
            orderDialog?.dismiss()
        }
    }
}
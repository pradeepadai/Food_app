package com.example.foododeringanddeliveryapp.ui.buyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.data.BuyerViewModel
import com.example.foododeringanddeliveryapp.models.User
import kotlinx.android.synthetic.main.fragment_display_shops.*


class DisplayShopsFragment : Fragment(), ShopRecyclerViewAdapter.OnShopClickListener {

    private val buyerVm: BuyerViewModel by viewModels()
    private val mAdapter by lazy { ShopRecyclerViewAdapter(null, this) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (buyerVm.users.value == null) {
            buyerVm.getSellers()
        }
        buyerVm.users.observe(requireActivity(), {
            if (it.isNullOrEmpty())
                tvNoShops.visibility = View.VISIBLE
           // else mAdapter.updateList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_shops, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDisplayFoods.layoutManager = LinearLayoutManager(requireContext())
        rvDisplayFoods.adapter = mAdapter
    }

    override fun onClickShop(seller: User) {
        val action = DisplayShopsFragmentDirections.buyFoodFromShop(seller.uid)
        findNavController().navigate(action)
    }
}
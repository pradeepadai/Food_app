package com.joytekmotion.yemilicious.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.joytekmotion.yemilicious.R
import kotlinx.android.synthetic.main.fragment_role_setup.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
const val BUYER_ROLE = "buyer"
const val SELLER_ROLE = "seller"

class RoleSetupFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_role_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var role = ""

        // Select Buy Button
        btnBuy.setOnClickListener {
            highlightSelectedButton(btnBuy, btnSell)
            btnNext.isEnabled
            role = BUYER_ROLE
        }

        // Select Sell Button
        btnSell.setOnClickListener {
            highlightSelectedButton(btnSell, btnBuy)
            btnNext.isEnabled = true
            role = SELLER_ROLE
        }

        btnNext.setOnClickListener {
            val action = RoleSetupFragmentDirections.roleSetupToProfileSetup(role)
            findNavController().navigate(action)
        }
    }

    private fun highlightSelectedButton(selectButton: CardView, unselectButton: CardView) {
        selectButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_light))
        unselectButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }
}
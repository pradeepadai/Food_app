package com.example.foododeringanddeliveryapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.data.ProfileViewModel
import com.example.foododeringanddeliveryapp.models.Shop
import com.example.foododeringanddeliveryapp.models.User
import com.example.foododeringanddeliveryapp.ui.buyer.BuyersDashboardActivity
import com.example.foododeringanddeliveryapp.ui.seller.SellerDashboardActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile_setup.*
import kotlinx.android.synthetic.main.fragment_profile_setup.view.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
private const val TAG = "ProfileSetupFragment"
class ProfileSetupFragment : Fragment() {

    private val args: ProfileSetupFragmentArgs by navArgs()
    private val updateProfileVm: ProfileViewModel by viewModels()
    private val currentUser = Firebase.auth.currentUser

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile_setup, container, false)
        if (args.userRole == BUYER_ROLE) {
            root.edtShopName.isVisible = false
            root.edtAddress.hint = getString(R.string.home_address)
        }
        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateProfileVm.role.observe(viewLifecycleOwner, {
            if (it == BUYER_ROLE)
                startActivity(Intent(requireContext(), BuyersDashboardActivity::class.java))
            else
                startActivity(Intent(requireContext(), SellerDashboardActivity::class.java))
        })
        updateProfileVm.updateFailed.observe(
            viewLifecycleOwner,
            { Log.d(TAG, "onViewCreated: Profile cannot be updated") })

        // Complete App Setup
        btnFinishSetup.setOnClickListener {
            if (currentUser != null) {
                val user = User()

                val address = edtAddress.text.toString().trim()
                val phoneNumber = edtPhoneNumber.text.toString().trim()

                if (args.userRole == BUYER_ROLE) {
                    user.address = address
                    user.role = BUYER_ROLE
                } else {
                    user.shop = Shop(edtShopName.text.toString().trim(), address)
                    user.role = SELLER_ROLE
                }
                user.email = currentUser.email!!
                currentUser.displayName!!.also { user.name = it }
                user.uid = currentUser.uid
                user.phone = phoneNumber
                updateProfileVm.updateProfile(user)
            }
        }


        // Navigate to the roles fragment
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.RoleSetupFragment)
        }
    }
}
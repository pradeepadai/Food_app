package com.example.foododeringanddeliveryapp.ui.seller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.data.LoginViewModel
import com.example.foododeringanddeliveryapp.ui.LoginActivity
import kotlinx.android.synthetic.main.activity_seller_dashboard.*

private const val TAG = "SellerDashboardActivity"
class SellerDashboardActivity : AppCompatActivity()  {

    private val loginVm: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_dashboard)
        setSupportActionBar(sellerDashboardToolbar)

        Log.d(TAG, "onCreate: starts")

        loginVm.currentUser.observe(this, {
            if (it == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        })

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.seller_nav_host_fragment) as NavHostFragment

        val navController = navHost.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.DisplayShopFragment, R.id.BuyFoodFragment
            )
        )
        sellerDashboardToolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.seller_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuAddFood -> {
                startActivity(Intent(this, AddFoodActivity::class.java))
            }
            R.id.mnuLogout -> loginVm.logout()
        }
        return super.onOptionsItemSelected(item)
    }

}
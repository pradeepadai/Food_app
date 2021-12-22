package com.joytekmotion.yemilicious.ui.buyer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.joytekmotion.yemilicious.R
import com.joytekmotion.yemilicious.data.LoginViewModel
import com.joytekmotion.yemilicious.ui.LoginActivity
import kotlinx.android.synthetic.main.activity_buyers_dashboard.*

class BuyersDashboardActivity : AppCompatActivity() {
    private val loginVm: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyers_dashboard)
        setSupportActionBar(findViewById(R.id.toolbar))

        loginVm.currentUser.observe(this, {
            if (it == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        })

        val navHost = supportFragmentManager.findFragmentById(R.id.buyerFragment) as NavHostFragment

        val navController = navHost.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_orders, R.id.navigation_settings
            )
        )
        toolbar.setupWithNavController(navController, appBarConfiguration)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.buyer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuLogout -> loginVm.logout()
            R.id.mnuMyOrders -> this.findNavController(R.id.buyerFragment)
                .navigate(R.id.buyerOrdersFragment)

        }
        return super.onOptionsItemSelected(item)
    }
}
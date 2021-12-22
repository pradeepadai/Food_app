package com.joytekmotion.yemilicious.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.joytekmotion.yemilicious.R
import kotlinx.android.synthetic.main.activity_setup.*

class SetupActivity : AppCompatActivity() {
    private val permissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private var counter = 0L

    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var nav_view: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer)
        val nav_view: NavigationView = findViewById(R.id.nav_view)
        // check for permission
        if (!hasPermission()) {
            requestPermission()
        }
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, SetupActivity::class.java))
                }
                R.id.terms -> {
                    startActivity(Intent(this, termsandservice::class.java))
                }
                R.id.rate -> {
                    startActivity(Intent(this, rateapp::class.java))
                }
                R.id.profile -> {
                    startActivity(Intent(this, profile::class.java))
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
            true
        }
    }
    override fun onBackPressed() {

        if (counter + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
            finish()
        }
        else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        counter = System.currentTimeMillis()
    }


    private fun logout() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Log Out!!")
        builder.setMessage("Are you sure do you want to Logout ?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") {_,_ ->
            userlogout()
        }
        builder.setNegativeButton("No") {_,_ ->
            Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show()
        }
        val alertDialog: android.app.AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun userlogout() {
        val sharedPref=this?.getPreferences(Context.MODE_PRIVATE)?:return
        val editsharedPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        val editor = editsharedPref.edit()
        editor.clear()
        editor.apply()

        sharedPref.edit().remove("Email").apply()
        var intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()

    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@SetupActivity,
            permissions, 1
        )
    }

    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}

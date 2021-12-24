package com.example.foododeringanddeliveryapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.foododeringanddeliveryapp.API.ServiceBuilder
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.Repository.UserRepository
import com.example.foododeringanddeliveryapp.data.LoginViewModel
import com.example.foododeringanddeliveryapp.helpers.alertBox
import com.example.foododeringanddeliveryapp.ui.buyer.BuyersDashboardActivity
import com.example.foododeringanddeliveryapp.ui.seller.SellerDashboardActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val loginVm: LoginViewModel by viewModels()
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var btnlogin: Button
    private lateinit var linkregister: TextView
    private lateinit var txtname: TextView
    private lateinit var txtpass: TextView
    private lateinit var linearLayout: LinearLayout
    private lateinit var forgotpass: TextView
    private lateinit var checkbox: CheckBox
    lateinit var sharedPreferences: SharedPreferences
    var isRemembered = false

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val actionBar = supportActionBar
        //actionBar!!.hide()

        btnlogin = findViewById(R.id.btnlogin)
        linkregister = findViewById(R.id.linkregister)
        txtname = findViewById(R.id.txtname)
        txtpass = findViewById(R.id.txtpass)
        forgotpass = findViewById(R.id.forgotpass)
        checkbox = findViewById(R.id.saveuser)
        auth = FirebaseAuth.getInstance()

        sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        if (isRemembered) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnlogin.setOnClickListener {
            login()

        }
        linkregister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }

        FirebaseApp.initializeApp(this)


        firebaseAuth = FirebaseAuth.getInstance()



        // TODO: login button animation

        // Observe current user to check if user has setup profile
        loginVm.currentUser.observe(this, {
            if (it == null) {
                alertBox(
                    loginContainer,
                    getString(R.string.login_to_continue),
                    Snackbar.LENGTH_LONG
                )
            } else {
                loginVm.checkSetup(it)
            }
        })

        // Observe if user has setup profile to change ui
        loginVm.userRole.observe(this, {
            if (it != null) {
                if (it == SELLER_ROLE)
                    launchSellerDashboardActivity()
                else
                    launchBuyerDashboardActivity()
            } else launchSetupActivity()
        })

        // Observe loginError to display error message
        loginVm.loginError.observe(this, {
            alertBox(loginContainer, it, Snackbar.LENGTH_LONG)
        })

        btnlogin.setOnClickListener {
            loginVm.login(txtname.text.toString().trim(), txtpass.text.toString().trim())
        }

        // Open Registration activity
        linkregister.setOnClickListener {
            registrationActivity()
        }
    }

    private fun login() {

        if (validateLogin()) {
            val email = txtname.text.toString()
            val password = txtpass.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        var intent =Intent(this,SetupActivity::class.java)
                        intent.putExtra("email",email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Wrong Details", Toast.LENGTH_LONG).show()
                    }
                }





            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = UserRepository()
                    val response = repository.loginUser(email, password)
                    if (response.success == true) {
                        ServiceBuilder.token = "Bearer " + response.token

                        savepref()

                        startActivity(
                            Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                        )
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        finish()
                    } else {
                        withContext(Dispatchers.Main) {
                            val snack =
                                Snackbar.make(
                                    linearLayout,
                                    "Invalid credentials",
                                    Snackbar.LENGTH_LONG
                                )
                            snack.setAction("OK", View.OnClickListener {
                                snack.dismiss()
                            })
                            snack.show()
                        }
                    }

                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            ex.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }

    }



    private fun savepref() {
        val checked: Boolean = checkbox.isChecked
        val edemail = txtname.text.toString()
        val edpassword = txtpass.text.toString()

        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", edemail)
        editor.putString("password", edpassword)
        editor.putBoolean("CHECKBOX", checked)
        editor.apply()


    }


    private fun sanitize(input: EditText): String {
        return input.text.toString().trim(' ')
    }

    private fun validateLogin(): Boolean {
        var valid = true
        txtname.error = null
        txtpass.error = null

        if (sanitize(txtname as EditText).isEmpty()) {
            txtname.error = "email can not be empty"
            valid = false
        }
        if (sanitize(txtpass as EditText).isEmpty()) {
            txtpass.error = "Password can not be empty"
            valid = false
        }

        return valid
    }


//    public override fun onStart() {
//        super.onStart()
//
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }

    fun updateUI(currentUser: FirebaseUser?) {

    }

    private fun registrationActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun launchSetupActivity() {
        startActivity(Intent(this, SetupActivity::class.java))
    }

    private fun launchSellerDashboardActivity() {
        startActivity(Intent(this, SellerDashboardActivity::class.java))
    }

    private fun launchBuyerDashboardActivity() {
        startActivity(Intent(this, BuyersDashboardActivity::class.java))
    }
}



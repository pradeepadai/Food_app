package com.joytekmotion.yemilicious.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joytekmotion.yemilicious.R
import com.joytekmotion.yemilicious.Repository.UserRepository
import com.joytekmotion.yemilicious.data.RegisterUsersViewModel
import com.joytekmotion.yemilicious.models.User
import com.joytekmotion.yemilicious.models.UsersContract
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class RegisterActivity : AppCompatActivity() {
    private lateinit var btnsignup: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    private lateinit var etname: TextView
    private lateinit var etphone: TextView
    private lateinit var etpass: TextView
    private lateinit var etconfigpass: TextView
    private lateinit var eetemail: TextView
    private lateinit var btnlog: TextView
    private val registerVm: RegisterUsersViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        etname = findViewById(R.id.etname)
        eetemail = findViewById(R.id.etemail)
        etpass = findViewById(R.id.etpass)
        etconfigpass = findViewById(R.id.etconpass)
        etphone = findViewById(R.id.etphone)
        btnsignup = findViewById(R.id.btnsignup)
        btnlog = findViewById(R.id.btnlog)
        db= FirebaseFirestore.getInstance()
        registerVm.validationErrors.observe(this, { t -> checkErrors(t) })
        registerVm.currentUser.observe(this, { startActivity(Intent(this, SetupActivity::class.java)) })
        registerVm.loginError.observe(this, { t -> Snackbar.make(registerLayout, "Error: $t", Snackbar.LENGTH_LONG).show() })

        // TODO: Button animation
//        bindProgressButton(regRegister)
//
//        regRegister.attachTextChangeAnimator {  }

        // Click register button to register user
        btnsignup.setOnClickListener {
            registerUser()

        }
        btnsignup.setOnClickListener{
            val FullName = etname.text.toString()

            val email = eetemail.text.toString()

            val phone = etphone.text.toString()
            val password = etpass.text.toString()

            val users = hashMapOf(
                "Name" to FullName,
                "Phone" to phone,
                "email" to email
            )
            val Users=db.collection("USERS")
            val query = Users.whereEqualTo("email", email).get()
                .addOnSuccessListener { tasks->
                    if(tasks.isEmpty)
                    {

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this){ task->
                                if(task.isSuccessful)
                                {
                                    Users.document(email).set(users)

                                    intent.putExtra("email",email)

                                    startActivity(intent)
                                    finish()
                                }
                                else
                                {
                                    Toast.makeText(
                                        this,
                                        "Authentication Failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                    else
                    {
                        Toast.makeText(this, "User Already Registered", Toast.LENGTH_LONG).show()
                        val intent= Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            val confirmPassword = etconfigpass.text.toString()
            if (eetemail.text.toString().isEmpty()) {
                eetemail.error = "Please enter email"
                eetemail.requestFocus()

            }

            if (!Patterns.EMAIL_ADDRESS.matcher(eetemail.text.toString()).matches()) {
                eetemail.error = "Please enter valid email"
                eetemail.requestFocus()

            }
            if (etphone.text.toString().isEmpty()) {
                etphone.error = "Please enter phone number"
                etphone.requestFocus()

            }
            if (etname.text.toString().isEmpty()) {
                etname.error = "Please enter your full name"
                etname.requestFocus()

            }

            if (etpass.text.toString().isEmpty()) {
                etpass.error = "Please enter password"
                etpass.requestFocus()

            }
            if (password != confirmPassword) {
                etpass.error = "Password does not match"
                etpass.requestFocus()
                return@setOnClickListener
            } else {
                val user =
                    com.example.tour_guide_nepal.ENTITY.User(

                        fullname = FullName,

                        email = email,

                        phone = phone,
                        password = password
                    )
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val userRepository = UserRepository()
                        val response = userRepository.registerUser(user)
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "signup successfully", Toast.LENGTH_SHORT

                                ).show()
                                getSharedPreferences(
                                    "shared_preference",
                                    MODE_PRIVATE
                                ).edit().putBoolean("isLoggedIn", true).apply()
                            }
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@RegisterActivity,
                                ex.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }

        }

        // Click sign in button to load login activity
        btnlog.setOnClickListener {
            loginActivity()
        }
    }

    private fun registerUser() {
        val user = User()
        user.name = etname.text.toString().trim()
        user.email = etemail.text.toString().trim()
        user.phone = etphone.text.toString().trim()
        user.password = etpass.text.toString().trim()
        registerVm.register(user, etconpass.text.toString().trim())
    }

    private fun checkErrors(errors: ContentValues) {
        etname.error = errors.getAsString(UsersContract.Fields.NAME)
        etemail.error = errors.getAsString(UsersContract.Fields.EMAIL)
        etphone.error = errors.getAsString(UsersContract.Fields.Phone)
        etpass.error = errors.getAsString(UsersContract.Fields.PASSWORD)
        etconpass.error = errors.getAsString(UsersContract.Fields.PASSWORD_CONFIRM)
    }

    private fun loginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

}
//class RegisterActivity : AppCompatActivity() {
//    private val registerVm: RegisterUsersViewModel by viewModels()

//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//

//
//
//
//        registerVm.currentUser.observe(this, { startActivity(Intent(this, SetupActivity::class.java)) })
//        registerVm.loginError.observe(this, { t -> Snackbar.make(registerLayout, "Error: $t", Snackbar.LENGTH_LONG).show() })
//
//        btnsignup.setOnClickListener {
//            registerUser()
//        }

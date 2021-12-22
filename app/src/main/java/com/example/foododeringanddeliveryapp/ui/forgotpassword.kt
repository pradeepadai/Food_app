package com.example.foododeringanddeliveryapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.foododeringanddeliveryapp.R
import com.google.firebase.auth.FirebaseAuth


class forgotpassword : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var et_forget_email: TextView
    private lateinit var btn_submit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)
        btn_submit = findViewById(R.id.btn_submit)
        et_forget_email = findViewById(R.id.et_forget_email)
        mAuth = FirebaseAuth.getInstance()
///this i the code of forget password for forgetting the user information
        //user can forget their information if they entering their wrong info

        btn_submit.setOnClickListener {
            val email = et_forget_email.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@forgotpassword, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
                            updateUI()
                        } else {
                            Toast.makeText(this@forgotpassword, "No user found with this email!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

    private fun updateUI() {
        val intent = Intent(this@forgotpassword, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}



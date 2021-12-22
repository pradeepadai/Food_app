package com.example.foododeringanddeliveryapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.example.foododeringanddeliveryapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private val DEFAULT_IMAGE_URL = "https://picsum.photos/200"
    private val REQUEST_IMAGE_CAPTURE = 100
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private lateinit var db: FirebaseFirestore
    private lateinit var imageUri: Uri
    private lateinit var backhome: LinearLayout
    private lateinit var imageView: ImageView
    private lateinit var camera: ImageView

    private lateinit var etfullname: TextView
    private lateinit var eefullname: TextView
    private lateinit var eeemail: TextView
    private lateinit var etemail: TextView
    private lateinit var etphone: TextView
    private lateinit var btnsave: Button
    private lateinit var choosephoto: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etfullname = findViewById(R.id.ffullname)
        eefullname = findViewById(R.id.ettvfullname)
        eeemail = findViewById(R.id.ettvemail)
        etemail = findViewById(R.id.eemail)
        etphone = findViewById(R.id.pphone)
        backhome = findViewById(R.id.backhome)
        camera = findViewById(R.id.camera)
        btnsave = findViewById(R.id.btnsave)
        choosephoto = findViewById(R.id.choosephoto)


        val sharedPref = Activity().getPreferences(Context.MODE_PRIVATE)
        val isLogin = sharedPref.getString("Email", "1")

        if (isLogin == "1") {
            var email = Activity().intent.getStringExtra("email")
            if (email != null) {
                setText(email)
                with(sharedPref.edit())
                {
                    putString("Email", email)
                    apply()
                }
            } else {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }
        } else {
            setText(isLogin)
        }

        backhome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        choosephoto.setOnClickListener {


        }
        btnsave.setOnClickListener {

        }

    }

        private fun setText(email: String?) {
            db = FirebaseFirestore.getInstance()
            if (email != null) {
                db.collection("USERS").document(email).get()
                    .addOnSuccessListener { tasks ->
                        ffullname.text = tasks.get("Name").toString()
                        ettvfullname.text = tasks.get("Name").toString()
                        ettvemail.text = tasks.get("email").toString()
                        pphone.text = tasks.get("Phone").toString()
                        eemail.text = tasks.get("email").toString()

                    }
            }

        }
    }

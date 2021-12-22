package com.example.foododeringanddeliveryapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.foododeringanddeliveryapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream

@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class profile : AppCompatActivity() {
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

    @SuppressLint("SetText18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        etfullname = findViewById(R.id.ffullname)
        eefullname = findViewById(R.id.ettvfullname)
        eeemail = findViewById(R.id.ettvemail)
        etemail = findViewById(R.id.eemail)
        etphone = findViewById(R.id.pphone)
        backhome = findViewById(R.id.backhome)
        camera = findViewById(R.id.camera)
        btnsave = findViewById(R.id.btnsave)
        choosephoto = findViewById(R.id.choosephoto)


        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val isLogin = sharedPref.getString("Email", "1")

        if (isLogin == "1") {
            var email = intent.getStringExtra("email")
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

            intentcamera()
        }
        btnsave.setOnClickListener {

            val photo = when {
                ::imageUri.isInitialized -> imageUri
                currentUser?.photoUrl == null -> Uri.parse(DEFAULT_IMAGE_URL)
                else -> currentUser.photoUrl
            }

            val name = ettvfullname.text.toString().trim()

            if (name.isEmpty()) {
                ettvfullname.error = "name required"
                ettvfullname.requestFocus()
                return@setOnClickListener
            }

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(photo)
                .build()

            progressbar.visibility = View.VISIBLE

            currentUser?.updateProfile(updates)
                ?.addOnCompleteListener { task ->
                    progressbar.visibility = View.INVISIBLE
                    if (task.isSuccessful) {

                    } else {

                    }
                }

        }


        currentUser?.let { user ->
            Glide.with(this)
                .load(user.photoUrl)
                .into(camera)
        }
    }
    private fun intentcamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

        progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            progressbar_pic.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        camera.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let {

                }
            }
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


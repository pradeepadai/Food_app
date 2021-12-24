package com.example.foododeringanddeliveryapp.ui.seller

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.data.FoodViewModel
import com.example.foododeringanddeliveryapp.data.LoginViewModel
import com.example.foododeringanddeliveryapp.helpers.alertBox
import com.example.foododeringanddeliveryapp.models.Food
import com.example.foododeringanddeliveryapp.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_food.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val REQUEST_IMAGE_GET = 100
private const val TAG = "AddFoodActivity"

class AddFoodActivity : AppCompatActivity() {
    private val foodVm: FoodViewModel by viewModels()
    private val sellerVm: LoginViewModel by viewModels()
    private lateinit var food: Food
    private var oldFoodImagePath: String? = null
    private var oldFood: Food? = null
    private var selectedImageBitmap: Bitmap? = null
    private lateinit var seller: User
    private var foodId: String = ""
    private var imageName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        oldFood = intent.getParcelableExtra(FOOD)

        if (oldFood != null) {

            val editFood = oldFood
            setTitle(R.string.edit_food)
            btnSaveFood.isEnabled = true
            edtFoodName.setText(editFood?.name)
            edtFoodPrice.setText(editFood?.price.toString())
            edtFoodDescription.setText(editFood?.description)
            foodVm.getFoodImageURL(editFood?.image!!)
            foodId = editFood.id
            oldFood!!.id = ""
        }

        // Check for error while adding food to database
        foodVm.foodAddError.observe(this, {
            alertBox(addFoodContainer, it, Snackbar.LENGTH_LONG)
        })

        sellerVm.currentUserInfo.observe(this, {
            seller = it
        })

        // Check if food has been added to database
        foodVm.isFoodAdded.observe(this, {
            startActivity(Intent(this, SellerDashboardActivity::class.java))
        })

        foodVm.imageURL.observe(this, {
            oldFoodImagePath = oldFood?.image
            oldFood?.image = null
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.default_food)
                .error(R.drawable.default_food)
                .into(uploadFoodView)
            uploadFoodView.visibility = View.VISIBLE
        })

        // Check for errors
        foodVm.validationErrors.observe(this, {
            alertBox(addFoodContainer, it.toString(), Snackbar.LENGTH_LONG)
        })

        btnUploadFoodImage.setOnClickListener {
            // Load Intent to get image and upload image
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(packageManager) != null)
                startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        // Upload Image and Save food
        btnSaveFood.setOnClickListener {
            imageName = "${
                edtFoodName.text.toString().toLowerCase(Locale.getDefault()).replace(" ", "_")
                    .trim()
            }_${edtFoodPrice.text.toString().trim()}.jpg"
            var foodPrice = 0.0
            if (edtFoodPrice.text.isNotEmpty()) {
                foodPrice = edtFoodPrice.text.toString().trim().toDouble()
            }
            food = Food(
                edtFoodName.text.toString().trim(),
                foodPrice,
                edtFoodDescription.text.toString().trim(),
                null, seller, ""
            )

            when {
                oldFood == null -> {
                    // Add Food
                    Log.d(TAG, "new input")
                    uploadImage(imageName)
                }
                (isDirty() || (selectedImageBitmap != null)) -> {
                    // Edit Food
                    food.id = foodId
                    if (selectedImageBitmap == null) {
                        // Restore old image
                        food.image = oldFoodImagePath
                        foodVm.addFood(food)
                    } else {
                        removeOldImage(oldFoodImagePath)
                        uploadImage(imageName)
                    }
                }
                else -> startActivity(Intent(this, SellerDashboardActivity::class.java))
            }

        }
    }

    private fun isDirty(): Boolean {
        return (oldFood != food)
    }

    private fun uploadImage(imageName: String) {
        if (selectedImageBitmap != null) {
            val baos = ByteArrayOutputStream()
            selectedImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            Log.d(TAG, "onActivityResult: About to start upload")

            alertBox(addFoodContainer, "Uploading...please wait!", Snackbar.LENGTH_LONG)

            // Check if edit new image to remove image
            // Create firebase upload reference
            val uploadFoodRef = Firebase.storage
                .reference.child("foods/${imageName}")
            val uploadFoodTask = uploadFoodRef.putBytes(imageData)
            uploadFoodTask.addOnFailureListener {
                alertBox(addFoodContainer, it.message!!, Snackbar.LENGTH_LONG)
            }.addOnSuccessListener {
                food.image = it.storage.path
                foodVm.addFood(food)
            }
        }
    }

    private fun removeOldImage(oldFoodImagePath: String?) {
        Firebase.storage.reference.child(oldFoodImagePath!!)
            .delete().addOnSuccessListener {
                Log.d(TAG, "Old food image removed successfully!")
            }.addOnFailureListener {
                Log.d(TAG, "${it.message}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            // if the user wants to edit a new image
            if (selectedImageUri != null) {
                try {
                    selectedImageUri.let {
                        selectedImageBitmap = if (Build.VERSION.SDK_INT < 28) {
                            @Suppress("DEPRECATION")
                            MediaStore.Images.Media.getBitmap(
                                contentResolver,
                                selectedImageUri
                            )
                        } else {
                            val source =
                                ImageDecoder.createSource(contentResolver, selectedImageUri)
                            ImageDecoder.decodeBitmap(source)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // Display image in drawable
                uploadFoodView.visibility = View.VISIBLE
                uploadFoodView.setImageBitmap(selectedImageBitmap)
                btnSaveFood.isEnabled = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> startActivity(Intent(this, SellerDashboardActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
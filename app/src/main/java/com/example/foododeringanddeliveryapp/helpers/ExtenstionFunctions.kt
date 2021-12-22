package com.example.foododeringanddeliveryapp.helpers

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun alertBox(view: View, message: String, duration: Int) {
    Snackbar.make(view, message, duration).show()
}
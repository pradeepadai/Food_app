package com.joytekmotion.yemilicious.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import com.joytekmotion.yemilicious.R

class rateapp : AppCompatActivity() {
    private lateinit var rate: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var rateCount: TextView
    private lateinit var review: EditText
    private lateinit var submitBtn: Button
    private lateinit var showRating: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rateapp)
        rate = findViewById(R.id.rate)
        ratingBar = findViewById(R.id.ratingBar)
        rateCount = findViewById(R.id.rateCount)
        review = findViewById(R.id.review)
        submitBtn = findViewById(R.id.submitBtn)
        showRating = findViewById(R.id.showRating)


        ratingBar.setOnRatingBarChangeListener{ ratingBar: RatingBar, fl: Float, b: Boolean -> Ratingbar()};

        submitBtn.setOnClickListener {
            val temp = rateCount.getText().toString()
            showRating.setText("Your Rating For Our App: \n" + temp+ "\n" + review.getText());
            review.setText("");
            ratingBar.setRating(0F);
            rateCount.setText("")

        }

    }

    private fun Ratingbar() {
        val rateValue = ratingBar.rating.toString()
        if (rateValue<= 1.toString() && rateValue> 0.toString())
            rateCount.setText("Bad  " + rateValue + "/5");
        else if (rateValue<= 2.toString() && rateValue> 1.toString())
            rateCount.setText("Ok  " + rateValue + "/5");
        else if (rateValue<= 3.toString() && rateValue> 2.toString())
            rateCount.setText("Good  " + rateValue + "/5");
        else if (rateValue<= 4.toString() && rateValue> 3.toString())
            rateCount.setText("Very Good  " + rateValue + "/5");
        else if (rateValue<= 5.toString() && rateValue> 4.toString())
            rateCount.setText("Best  " + rateValue + "/5");
    }



}
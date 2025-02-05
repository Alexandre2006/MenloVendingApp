package com.example.menlovending

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DollarAmountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dollar_amount)

        // Retrieve the extra from the intent
        val dollarAmount = intent.getDoubleExtra("DOLLAR_AMOUNT", 0.0)

        // Display the dollarAmount
        val amountTextView = findViewById<TextView>(R.id.dollar_amount_text_view)
        amountTextView.text = String.format("$%.2f", dollarAmount)

        // Handle the Back button
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener { v: View? -> finish() } // Ends current activity and returns to MainActivity
    }
}

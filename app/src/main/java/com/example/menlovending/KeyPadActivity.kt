package com.example.menlovending

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KeyPadActivity : AppCompatActivity() {
    private var displayTextView: TextView? = null
    private val enteredCode = StringBuilder()
    private val prices = DoubleArray(ITEM_COUNT + 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        // Fill prices array with some dummy data
        for (i in 1..ITEM_COUNT) {
            prices[i] = 0.50 * i
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.display_text_view)
        val keypadGrid = findViewById<GridLayout>(R.id.keypad_grid)

        for (i in 1..9) {
            val button = Button(this)
            button.text = i.toString()
            button.textSize = 24f
            button.gravity = Gravity.CENTER
            button.setOnClickListener(NumberClickListener(i))
            keypadGrid.addView(button)
        }

        // Add a "Clear" button
        val clearButton = Button(this)
        clearButton.text = "CLEAR"
        clearButton.textSize = 18f
        clearButton.gravity = Gravity.CENTER
        clearButton.setOnClickListener { v: View? ->
            enteredCode.setLength(0)
            updateDisplay()
        }
        keypadGrid.addView(clearButton)

        // Add a zero button
        val button = Button(this)
        button.text = 0.toString()
        button.textSize = 24f
        button.gravity = Gravity.CENTER
        button.setOnClickListener(NumberClickListener(0))
        keypadGrid.addView(button)

        // Add an "Enter" button
        val enterButton = Button(this)
        enterButton.text = "ENTER"
        enterButton.textSize = 18f
        enterButton.gravity = Gravity.CENTER
        enterButton.setOnClickListener(EnterClickListener())
        keypadGrid.addView(enterButton)
    }

    private inner class NumberClickListener(private val number: Int) : View.OnClickListener {
        override fun onClick(v: View) {
            enteredCode.append(number)
            updateDisplay()
        }
    }

    private inner class EnterClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            // Get the entered code (dollar amount)
            val itemNum = enteredCode.toString().toInt()
            val dollarAmount: Double
            if (itemNum > 0 && itemNum < ITEM_COUNT + 1) {
                dollarAmount = prices[itemNum]
            } else {
                displayTextView!!.text = "Invalid Item Number"
                return
            }
            // Create an Intent to start the DollarAmountActivity
            val intent = Intent(
                this@KeyPadActivity,
                DollarAmountActivity::class.java
            )
            // Pass the dollar amount as an extra
            intent.putExtra("DOLLAR_AMOUNT", dollarAmount)

            // Start the new activity
            startActivity(intent)
        }
    }

    private fun updateDisplay() {
        displayTextView!!.text = enteredCode.toString()
    }

    companion object {
        const val ITEM_COUNT: Int = 16
    }
}

package com.example.menlovending.stripe.client

import android.app.Application
import com.stripe.stripeterminal.TerminalApplicationDelegate.onCreate

class StripeTerminalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        onCreate(this)
    }
}
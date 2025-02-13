package com.example.menlovending.stripe.client

import android.app.Application
import android.content.Context
import com.stripe.stripeterminal.TerminalApplicationDelegate.onCreate

class StripeTerminalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        onCreate(this)
    }

    companion object {
        // Instance
        private var instance: StripeTerminalApplication? = null

        // Context
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}
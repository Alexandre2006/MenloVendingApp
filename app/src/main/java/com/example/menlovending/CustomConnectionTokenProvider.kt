package com.example.menlovending

import com.stripe.Stripe
import com.stripe.model.terminal.ConnectionToken
import com.stripe.param.terminal.ConnectionTokenCreateParams
import com.stripe.stripeterminal.external.callable.ConnectionTokenCallback
import com.stripe.stripeterminal.external.callable.ConnectionTokenProvider
import com.stripe.stripeterminal.external.models.ConnectionTokenException

class CustomConnectionTokenProvider : ConnectionTokenProvider {
    override fun fetchConnectionToken(callback: ConnectionTokenCallback) {
        try {
            Stripe.apiKey = ""

            // Create connection token
            val params =
                ConnectionTokenCreateParams.builder()
                    .build()
            val connectionToken = ConnectionToken.create(params)

            // Return secret
            val secret = connectionToken.secret
            callback.onSuccess(secret)
        } catch (e: Exception) {
            callback.onFailure(
                ConnectionTokenException("Failed to fetch connection token", e)
            )
        }
    }
}
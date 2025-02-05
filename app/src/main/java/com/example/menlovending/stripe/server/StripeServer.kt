package com.example.menlovending.stripe.server

import com.stripe.Stripe
import com.stripe.exception.StripeException
import com.stripe.model.PaymentIntent
import com.stripe.model.terminal.ConnectionToken
import com.stripe.param.PaymentIntentCreateParams
import com.stripe.param.terminal.ConnectionTokenCreateParams

class StripeServer private constructor() {
    @get:Throws(StripeException::class)
    val connectionToken: String
        // Endpoints
        get() {
            val params = ConnectionTokenCreateParams.builder()
                .build()

            val connectionToken = ConnectionToken.create(params)
            return connectionToken.secret
        }

    @Throws(StripeException::class)
    fun createPaymentIntent(amount: Long?): String {
        val createParams = PaymentIntentCreateParams.builder()
            .setCurrency("usd")
            .setAmount(amount)
            .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
            .build()

        val intent = PaymentIntent.create(createParams)
        return intent.id
    }

    @Throws(StripeException::class)
    fun capturePaymentIntent(paymentIntentId: String): String {
        val intent = PaymentIntent.retrieve(paymentIntentId)
        val capturedIntent = intent.capture()
        return capturedIntent.id
    }

    // Constructor
    init {
        Stripe.apiKey = STRIPE_API_KEY
    }

    companion object {
        // Stripe API key (DO NOT COMMIT THIS, REPLACE WITH ENVIRONMENT VARIABLE)
        private const val STRIPE_API_KEY =
            "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"

        // Instance
        val instance: StripeServer = StripeServer()
    }
}

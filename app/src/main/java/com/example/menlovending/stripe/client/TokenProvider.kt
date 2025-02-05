package com.example.menlovending.stripe.client

import com.example.menlovending.stripe.server.StripeServer
import com.stripe.exception.StripeException
import com.stripe.stripeterminal.external.callable.ConnectionTokenCallback
import com.stripe.stripeterminal.external.callable.ConnectionTokenProvider
import com.stripe.stripeterminal.external.models.ConnectionTokenException

class TokenProvider : ConnectionTokenProvider {
    override fun fetchConnectionToken(callback: ConnectionTokenCallback) {
        try {
            val token: String = StripeServer.instance.connectionToken;
            callback.onSuccess(token)
        } catch (e: StripeException) {
            callback.onFailure(
                ConnectionTokenException(
                    (if (e.message == null) "Creating connection token failed" else e.message)!!,
                    e.cause
                )
            )
        }
    }
}

package edu.austral.dissis.server

import edu.austral.ingsis.clientserver.ClientConnectionListener

class ClientConnectionCollectorListener: ClientConnectionListener {
    var isConnected = false

    override fun handleConnection() {
        isConnected = true
    }

    override fun handleConnectionClosed() {
        isConnected = false
    }

    fun clear() {
        isConnected = false
    }
}
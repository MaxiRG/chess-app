package edu.austral.dissis.server

import edu.austral.ingsis.clientserver.ServerConnectionListener

class ServerConnectionCollectorListener: ServerConnectionListener {
    val clients = mutableListOf<String>()

    override fun handleClientConnection(clientId: String) {
        clients.add(clientId)
    }

    override fun handleClientConnectionClosed(clientId: String) {
        clients.remove(clientId)
    }

    fun clear() {
        clients.clear()
    }
}
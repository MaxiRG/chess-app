package edu.austral.dissis.client

import edu.austral.dissis.chess.gui.InitialState
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

class InitListener(private val client: ChessClient):MessageListener<InitialState> {
    override fun handleMessage(message: Message<InitialState>) {
        client.handleInitialState(message.payload)
    }
}
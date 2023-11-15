package edu.austral.dissis.client

import edu.austral.dissis.chess.gui.InvalidMove
import edu.austral.ingsis.clientserver.Client
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

class InvalidMoveListener(private val client: ChessClient): MessageListener<InvalidMove> {
    override fun handleMessage(message: Message<InvalidMove>) {
        client.handleMoveResult(message.payload)
    }
}
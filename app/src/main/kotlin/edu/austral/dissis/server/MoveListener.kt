package edu.austral.dissis.server

import edu.austral.dissis.chess.gui.Move
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener
import edu.austral.ingsis.clientserver.Server

class MoveListener(private val server: ChessServer):MessageListener<Move> {
    override fun handleMessage(message: Message<Move>) {
        server.handleMove(message)
    }


}

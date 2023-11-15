package edu.austral.dissis.server

import edu.austral.dissis.chess.gui.GameEventListener
import edu.austral.dissis.chess.gui.Move
import edu.austral.ingsis.clientserver.Client
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.Server

class ChessMoveListener(private val client: Client, private val server: Server): GameEventListener {
    override fun handleMove(move: Move) {
        client.send(Message("move", move))
        client.closeConnection()
        server.stop()
    }
}
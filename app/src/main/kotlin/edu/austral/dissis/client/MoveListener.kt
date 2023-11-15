package edu.austral.dissis.client

import edu.austral.dissis.chess.gui.GameEventListener
import edu.austral.dissis.chess.gui.Move

class MoveListener(private val client: ChessClient): GameEventListener {
    override fun handleMove(move: Move) {
        client.move(move)
    }
}
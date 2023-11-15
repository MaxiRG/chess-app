package edu.austral.dissis.client

import edu.austral.dissis.chess.gui.GameOver
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.MessageListener

class GameOverListener(private val chessClient: ChessClient):MessageListener<GameOver> {
    override fun handleMessage(message: Message<GameOver>) {
        chessClient.handleMoveResult(message.payload)
    }

}

package edu.austral.dissis.client

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.InitialState
import edu.austral.dissis.chess.gui.Move
import edu.austral.dissis.chess.gui.MoveResult
import edu.austral.ingsis.clientserver.Message
import edu.austral.ingsis.clientserver.netty.client.NettyClientBuilder
import javafx.application.Platform
import java.net.InetSocketAddress

class ChessClient(private val gameView: GameView) {
    private val client =
            NettyClientBuilder.createDefault()
            .withAddress(InetSocketAddress(8080))
            .addMessageListener("invalid", jacksonTypeRef(), InvalidMoveListener(this))
            .addMessageListener("game-over", jacksonTypeRef(), GameOverListener(this))
            .addMessageListener("new-game-state", jacksonTypeRef(), ValidMoveListener(this))
            .addMessageListener("init", jacksonTypeRef(), InitListener(this))
            .build();


    init {
        client.connect()
        Thread.sleep(200)
        client.send(Message("init", Unit))
    }
    fun handleMoveResult(moveResult: MoveResult){
        gameView.handleMoveResult(moveResult)
    }

    fun move(move: Move){
        client.send(Message("move", move))
    }

    fun handleInitialState(payload: InitialState) {
        Platform.runLater {
            gameView.handleInitialState(payload);
        }

    }
}
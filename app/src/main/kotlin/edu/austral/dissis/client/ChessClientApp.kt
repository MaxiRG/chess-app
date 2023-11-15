package edu.austral.dissis.client

import edu.austral.dissis.chess.gui.CachedImageResolver
import edu.austral.dissis.chess.gui.DefaultImageResolver
import edu.austral.dissis.chess.gui.GameView
import edu.austral.dissis.chess.gui.createGameViewFrom
import edu.austral.ingsis.clientserver.Message
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

fun main() {
    Application.launch(ChessClientApp::class.java)
}

class ChessClientApp : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    companion object {
        const val GameTitle = "Chess"
    }
    override fun start(primaryStage: Stage) {
        primaryStage.title = GameTitle
        val root = GameView(imageResolver)
        primaryStage.scene = Scene(root)

        val client = ChessClient(root)
        root.addListener(MoveListener(client))
        primaryStage.show()
    }
}
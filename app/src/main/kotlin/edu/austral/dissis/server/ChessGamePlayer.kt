package edu.austral.dissis.server

import ChessGameApplication
import edu.austral.ingsis.clientserver.Client
import edu.austral.ingsis.clientserver.Server
import javafx.application.Application

class ChessGamePlayer(val client: Client) {

    fun startPlaying() {
        client.connect()
        Application.launch(ChessGameApplication::class.java)
    }
}
package rules

import Board

interface Rules {
    val startingPositions: Board
    fun checkWon(board: Board): Int
}
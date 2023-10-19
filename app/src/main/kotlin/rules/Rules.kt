package rules

import Board
import Match
import MyMove

interface Rules {
    val startingPositions: Board
    fun checkWon(match: Match): Boolean
    fun isInCheck(board:Board, turn: Boolean, moveHistory: List<Board>): Boolean
}
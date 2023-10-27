package validator

import Board
import MyMove
import piece.GetPieceMoveResult

interface Validator {
    fun validate(move: MyMove, board: Board, moveHistory: List<Board>):GetPieceMoveResult
}
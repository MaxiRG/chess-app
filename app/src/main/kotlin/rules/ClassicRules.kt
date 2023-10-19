package rules

import Board
import Coordinates
import Match
import MyMove
import piece.*

class ClassicRules():Rules {
    override val startingPositions = Board(mapOf(
        (Coordinates(0,0) to Piece(1, Rook(), true)),
        (Coordinates(1,0) to Piece(2, Knight(), true)),
        (Coordinates(2,0) to Piece(3, Bishop(), true)),
        (Coordinates(3,0) to Piece(4, Queen(), true)),
        (Coordinates(4,0) to Piece(5, King(), true)),
        (Coordinates(5,0) to Piece(6, Bishop(), true)),
        (Coordinates(6,0) to Piece(7, Knight(), true)),
        (Coordinates(7,0) to Piece(8, Rook(), true)),
        (Coordinates(0,1) to Piece(9, Pawn(), true)),
        (Coordinates(1,1) to Piece(10, Pawn(), true)),
        (Coordinates(2,1) to Piece(11, Pawn(), true)),
        (Coordinates(3,1) to Piece(12, Pawn(), true)),
        (Coordinates(4,1) to Piece(13, Pawn(), true)),
        (Coordinates(5,1) to Piece(14, Pawn(), true)),
        (Coordinates(6,1) to Piece(15, Pawn(), true)),
        (Coordinates(7,1) to Piece(16, Pawn(), true)),

        (Coordinates(0,7) to Piece(17, Rook(), false)),
        (Coordinates(1,7) to Piece(18, Knight(), false)),
        (Coordinates(2,7) to Piece(19, Bishop(), false)),
        (Coordinates(3,7) to Piece(20, Queen(), false)),
        (Coordinates(4,7) to Piece(21, King(), false)),
        (Coordinates(5,7) to Piece(22, Bishop(), false)),
        (Coordinates(6,7) to Piece(23, Knight(), false)),
        (Coordinates(7,7) to Piece(24, Rook(), false)),
        (Coordinates(0,6) to Piece(25, Pawn(), false)),
        (Coordinates(1,6) to Piece(26, Pawn(), false)),
        (Coordinates(2,6) to Piece(27, Pawn(), false)),
        (Coordinates(3,6) to Piece(28, Pawn(), false)),
        (Coordinates(4,6) to Piece(29, Pawn(), false)),
        (Coordinates(5,6) to Piece(30, Pawn(), false)),
        (Coordinates(6,6) to Piece(31, Pawn(), false)),
        (Coordinates(7,6) to Piece(32, Pawn(), false)),
    ), 7, 7)
    override fun checkWon(match: Match): Boolean {
        val map = match.board.positions
        val board = match.board
        val turn = match.playerTurn

        for((coordinates, piece) in map){
            if(piece.player == turn){
                for(x in 0..board.length){
                    for(y in 0..board.height) {
                        val move = MyMove(turn, coordinates, Coordinates(x, y))
                        val moveHistory: List<Board> = match.moveHistory
                        val newBoard: Board = when (val isValidMove = piece.isValidMove(move, board, moveHistory)) {
                            is GetCastleMoveResult -> continue
                            is GetInvalidMoveResult -> continue
                            is GetNormalMoveResult -> match.makeMove(move);
                            is GetPromotionMoveResult -> continue
                        }
                        if (!isInCheck(newBoard, turn, moveHistory)) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    override fun isInCheck(board: Board, turn: Boolean, moveHistory: List<Board>): Boolean {
        val map = board.positions
        val kingCoordinates = findKing(board, turn)

        for((coordinates, piece) in map){
            val checkMove = MyMove(turn, coordinates, kingCoordinates)
            return when(val moveResult = piece.isValidMove(checkMove, board, moveHistory)){
                is GetCastleMoveResult -> true
                is GetInvalidMoveResult -> continue
                is GetNormalMoveResult -> true
                is GetPromotionMoveResult -> true
            }
        }
        return false
    }

    private fun findKing(board: Board, turn: Boolean): Coordinates {
        val map = board.positions
        for ((coordinates, piece) in map){
            if(piece.pieceType == King() && piece.player == turn){
                return coordinates
            }
        }
        throw NoSuchElementException("Could not find king")
    }
}

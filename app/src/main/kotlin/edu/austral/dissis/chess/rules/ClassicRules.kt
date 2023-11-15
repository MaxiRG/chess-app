package edu.austral.dissis.chess.rules

import edu.austral.dissis.chess.validator.*
import edu.austral.dissis.commons.Board
import edu.austral.dissis.commons.BoardGenerator
import edu.austral.dissis.commons.Coordinates
import edu.austral.dissis.commons.GetInvalidMoveResult
import edu.austral.dissis.commons.GetNormalMoveResult
import edu.austral.dissis.commons.Match
import edu.austral.dissis.commons.MyMove
import edu.austral.dissis.commons.Piece
import edu.austral.dissis.commons.Vector
import edu.austral.dissis.commons.Rules

class ClassicRules(): Rules {
    private val boardGenerator = BoardGenerator();
    companion object {
        val QUEEN = listOf(
            MoveValidator(Vector.up, -1, true, true), MoveValidator(Vector.down, -1, true, true), MoveValidator(
            Vector.left, -1, true, true), MoveValidator(Vector.right, -1, true, true), MoveValidator(Vector.upLeft, -1, true, true), MoveValidator(
            Vector.upRight, -1, true, true), MoveValidator(Vector.downLeft, -1, true, true), MoveValidator(Vector.downRight, -1, true, true)
        )
        val BISHOP = listOf(
            MoveValidator(Vector.upLeft, -1, true, true),
            MoveValidator(Vector.upRight, -1, true, true),
            MoveValidator(Vector.downLeft, -1, true, true),
            MoveValidator(Vector.downRight, -1, true, true)
        )
        val ROOK = listOf(
            MoveValidator(Vector.up, -1, true, true), MoveValidator(Vector.down, -1, true, true), MoveValidator(
            Vector.left, -1, true, true), MoveValidator(Vector.right, -1, true, true)
        )
        val KNIGHT = listOf(
            VectorValidator(
            listOf(
                Vector(1,2), Vector(-1, 2), Vector(2, 1), Vector(2, -1), Vector(-2, 1), Vector(-2, -1), Vector(1, -2), Vector(-1, -2)
            ), true)
        )
        val KING = listOf(
            MoveValidator(Vector.up, 1, true, true), MoveValidator(Vector.down, 1, true, true), MoveValidator(
            Vector.left, 1, true, true), MoveValidator(Vector.right, 1, true, true), MoveValidator(Vector.upLeft, 1, true, true), MoveValidator(
            Vector.upRight, 1, true, true), MoveValidator(Vector.downLeft, 1, true, true), MoveValidator(Vector.downRight, 1, true, true), FirstMoveValidator(CastleValidator())
        )
        val PAWN = listOf(
            PromotionValidator(MoveValidator(Vector.upLeft, 1, true, false), QUEEN), PromotionValidator(MoveValidator(Vector.upRight, 1, true, false), QUEEN), PromotionValidator(
            MoveValidator(Vector.up, 1, false, true), QUEEN
            ), FirstMoveValidator(MoveValidator(Vector.up, 2, false, true))
        )

        val BPAWN = listOf(
            PromotionValidator(MoveValidator(Vector.downLeft, 1, true, false), QUEEN), PromotionValidator(MoveValidator(Vector.downRight, 1, true, false), QUEEN), PromotionValidator(
                MoveValidator(Vector.down, 1, false, true), QUEEN
            ), FirstMoveValidator(MoveValidator(Vector.down, 2, false, true))
        )
    }
    override val startingPositions = Board(mapOf(
        (Coordinates(0,0) to Piece(1, ROOK, true)),
        (Coordinates(1,0) to Piece(2, KNIGHT, true)),
        (Coordinates(2,0) to Piece(3, BISHOP, true)),
        (Coordinates(4,0) to Piece(4, QUEEN, true)),
        (Coordinates(3,0) to Piece(5, KING, true)),
        (Coordinates(5,0) to Piece(6, BISHOP, true)),
        (Coordinates(6,0) to Piece(7, KNIGHT, true)),
        (Coordinates(7,0) to Piece(8, ROOK, true)),
        (Coordinates(0,1) to Piece(9, PAWN, true)),
        (Coordinates(1,1) to Piece(10, PAWN, true)),
        (Coordinates(2,1) to Piece(11, PAWN, true)),
        (Coordinates(3,1) to Piece(12, PAWN, true)),
        (Coordinates(4,1) to Piece(13, PAWN, true)),
        (Coordinates(5,1) to Piece(14, PAWN, true)),
        (Coordinates(6,1) to Piece(15, PAWN, true)),
        (Coordinates(7,1) to Piece(16, PAWN, true)),

        (Coordinates(0,7) to Piece(17, ROOK, false)),
        (Coordinates(1,7) to Piece(18, KNIGHT, false)),
        (Coordinates(2,7) to Piece(19, BISHOP, false)),
        (Coordinates(4,7) to Piece(20, QUEEN, false)),
        (Coordinates(3,7) to Piece(21, KING, false)),
        (Coordinates(5,7) to Piece(22, BISHOP, false)),
        (Coordinates(6,7) to Piece(23, KNIGHT, false)),
        (Coordinates(7,7) to Piece(24, ROOK, false)),
        (Coordinates(0,6) to Piece(25, BPAWN, false)),
        (Coordinates(1,6) to Piece(26, BPAWN, false)),
        (Coordinates(2,6) to Piece(27, BPAWN, false)),
        (Coordinates(3,6) to Piece(28, BPAWN, false)),
        (Coordinates(4,6) to Piece(29, BPAWN, false)),
        (Coordinates(5,6) to Piece(30, BPAWN, false)),
        (Coordinates(6,6) to Piece(31, BPAWN, false)),
        (Coordinates(7,6) to Piece(32, BPAWN, false)),
    ), 7, 7)
    override fun checkWon(match: Match): Boolean {
        val map = match.board.positions
        val board = match.board
        val turn = match.playerTurn

        if(!isInCheck(board, turn, match.moveHistory)){
            return false
        }

        for((coordinates, piece) in map){
            if(piece.player == turn){
                for(x in 0..board.length){
                    for(y in 0..board.height) {
                        val move = MyMove(turn, coordinates, Coordinates(x, y))
                        val moveHistory: List<Board> = match.moveHistory
                        val newBoard: Board = when (val isValidMove = boardGenerator.generateBoard(piece, move, board, moveHistory)) {
                            is GetInvalidMoveResult -> continue
                            is GetNormalMoveResult -> isValidMove.board;
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
            return when(val moveResult = boardGenerator.generateBoard(piece, checkMove, board, moveHistory)){
                is GetInvalidMoveResult -> continue
                is GetNormalMoveResult -> true
            }
        }
        return false
    }

    private fun findKing(board: Board, turn: Boolean): Coordinates {
        val map = board.positions
        for ((coordinates, piece) in map){
            if(piece.validators == KING && piece.player == turn){
                return coordinates
            }
        }
        throw NoSuchElementException("Could not find king")
    }
}

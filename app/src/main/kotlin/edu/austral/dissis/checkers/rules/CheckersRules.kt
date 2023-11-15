package edu.austral.dissis.checkers.rules

import edu.austral.dissis.checkers.validator.CheckersValidator
import edu.austral.dissis.commons.Board
import edu.austral.dissis.commons.Coordinates
import edu.austral.dissis.commons.Match
import edu.austral.dissis.commons.Piece
import edu.austral.dissis.commons.Vector
import edu.austral.dissis.commons.Rules
import edu.austral.dissis.chess.rules.ClassicRules.Companion.PAWN
import edu.austral.dissis.chess.validator.MoveValidator
import edu.austral.dissis.chess.validator.PromotionValidator

class CheckersRules: Rules {

    companion object {

        val QUEEN_CHECKER = listOf(
            MoveValidator(Vector.upLeft, -1, true, true),
            MoveValidator(Vector.upRight, -1, true, true),
            MoveValidator(Vector.downLeft, -1, true, true),
            MoveValidator(Vector.downRight, -1, true, true),
            CheckersValidator(Vector.downRight, -1, true, true),
            CheckersValidator(Vector.upLeft, -1, true, true),
            CheckersValidator(Vector.upRight, -1, true, true),
            CheckersValidator(Vector.downLeft, -1, true, true)
        )

        val RCHECKER  = listOf(
            PromotionValidator(CheckersValidator(Vector.upLeft, 2, true, true), QUEEN_CHECKER),
            PromotionValidator(CheckersValidator(Vector.upRight, 2, true, true), QUEEN_CHECKER),
            PromotionValidator(MoveValidator(Vector.upRight, 1, false, true), QUEEN_CHECKER),
            PromotionValidator(MoveValidator(Vector.upLeft, 1, false, true), QUEEN_CHECKER),

        )
        val BCHECKER = listOf(
            PromotionValidator(CheckersValidator(Vector.downLeft, 2, true, true), QUEEN_CHECKER),
            PromotionValidator(CheckersValidator(Vector.downRight, 2, true, true), QUEEN_CHECKER),
            PromotionValidator(MoveValidator(Vector.downRight, 1, false, true), QUEEN_CHECKER),
            PromotionValidator(MoveValidator(Vector.downLeft, 1, false, true), QUEEN_CHECKER),

        )
    }
    override val startingPositions = Board(mapOf(
        Coordinates(1,0) to Piece(1, RCHECKER, true),
        Coordinates(3,0) to Piece(2, RCHECKER, true),
        Coordinates(5,0) to Piece(3, RCHECKER, true),
        Coordinates(7,0) to Piece(4, RCHECKER, true),
        Coordinates(0,1) to Piece(5, RCHECKER, true),
        Coordinates(2,1) to Piece(6, RCHECKER, true),
        Coordinates(4,1) to Piece(7, RCHECKER, true),
        Coordinates(6,1) to Piece(8, RCHECKER, true),
        Coordinates(1,2) to Piece(9, RCHECKER, true),
        Coordinates(3,2) to Piece(10, RCHECKER, true),
        Coordinates(5,2) to Piece(11, RCHECKER, true),
        Coordinates(7,2) to Piece(12, RCHECKER, true),

        Coordinates(0,7) to Piece(59, BCHECKER, false),
        Coordinates(2,7) to Piece(69, BCHECKER, false),
        Coordinates(4,7) to Piece(79, BCHECKER, false),
        Coordinates(6,7) to Piece(89, BCHECKER, false),
        Coordinates(1,6) to Piece(99, BCHECKER, false),
        Coordinates(3,6) to Piece(19, BCHECKER, false),
        Coordinates(5,6) to Piece(18, BCHECKER, false),
        Coordinates(7,6) to Piece(17, BCHECKER, false),
        Coordinates(0,5) to Piece(50, BCHECKER, false),
        Coordinates(2,5) to Piece(60, BCHECKER, false),
        Coordinates(4,5) to Piece(70, BCHECKER, false),
        Coordinates(6,5) to Piece(80, BCHECKER, false),

    ), 7, 7)
    override fun checkWon(match: Match): Boolean {
        for((coordinate, piece) in match.board.positions){
            if(piece.player != !match.playerTurn) return false
        }
        return true
    }

    override fun isInCheck(board: Board, turn: Boolean, moveHistory: List<Board>): Boolean {
        return false;
    }


}
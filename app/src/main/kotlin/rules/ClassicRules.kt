package rules

import Board
import Coordinates
import piece.*

class ClassicRules():Rules {
    override val startingPositions = Board(mapOf(
        (Coordinates(0,0) to Piece(Rook(), true, Coordinates(0,0))),
        (Coordinates(1,0) to Piece(Knight(), true, Coordinates(1,0))),
        (Coordinates(2,0) to Piece(Bishop(), true, Coordinates(2,0))),
        (Coordinates(3,0) to Piece(Queen(), true, Coordinates(3,4))),
        (Coordinates(4,0) to Piece(King(), true, Coordinates(4,0))),
        (Coordinates(5,0) to Piece(Bishop(), true, Coordinates(5,0))),
        (Coordinates(6,0) to Piece(Knight(), true, Coordinates(6,0))),
        (Coordinates(7,0) to Piece(Rook(), true, Coordinates(7,0))),
        (Coordinates(0,1) to Piece(Pawn(), true, Coordinates(0,1))),
        (Coordinates(1,1) to Piece(Pawn(), true, Coordinates(1,1))),
        (Coordinates(2,1) to Piece(Pawn(), true, Coordinates(2,1))),
        (Coordinates(3,1) to Piece(Pawn(), true, Coordinates(3,1))),
        (Coordinates(4,1) to Piece(Pawn(), true, Coordinates(4,1))),
        (Coordinates(5,1) to Piece(Pawn(), true, Coordinates(5,1))),
        (Coordinates(6,1) to Piece(Pawn(), true, Coordinates(6,1))),
        (Coordinates(7,1) to Piece(Pawn(), true, Coordinates(7,1))),

        (Coordinates(0,7) to Piece(Rook(), false, Coordinates(0,7))),
        (Coordinates(1,7) to Piece(Knight(), false, Coordinates(1,7))),
        (Coordinates(2,7) to Piece(Bishop(), false, Coordinates(2,7))),
        (Coordinates(3,7) to Piece(Queen(), false, Coordinates(3,7))),
        (Coordinates(4,7) to Piece(King(), false, Coordinates(4,7))),
        (Coordinates(5,7) to Piece(Bishop(), false, Coordinates(5,7))),
        (Coordinates(6,7) to Piece(Knight(), false, Coordinates(6,7))),
        (Coordinates(7,7) to Piece(Rook(), false, Coordinates(7,7))),
        (Coordinates(0,6) to Piece(Pawn(), false, Coordinates(0,6))),
        (Coordinates(1,6) to Piece(Pawn(), false, Coordinates(1,6))),
        (Coordinates(2,6) to Piece(Pawn(), false, Coordinates(2,6))),
        (Coordinates(3,6) to Piece(Pawn(), false, Coordinates(3,6))),
        (Coordinates(4,6) to Piece(Pawn(), false, Coordinates(4,6))),
        (Coordinates(5,6) to Piece(Pawn(), false, Coordinates(5,6))),
        (Coordinates(6,6) to Piece(Pawn(), false, Coordinates(6,6))),
        (Coordinates(7,6) to Piece(Pawn(), false, Coordinates(7,6))),
    ), 7, 7)
    override fun checkWon(board: Board): Int {

        TODO("Not yet implemented")
    }

    private fun isInCheck(board: Board): Boolean {
        return false
    }
}

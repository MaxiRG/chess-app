package validator

import Board
import EmptySquareResult
import GetPieceResult
import MyMove
import OutOfBoundResult
import Vector

data class VectorValidator(val vectors: List<Vector>):Validator {
    override fun validate(move: MyMove, board: Board): Boolean {
        return when(val objectiveCoordinates = board.getPieceInPosition(move.end)){
            is EmptySquareResult -> vectors.contains(Vector(move.end.x - move.start.x, move.end.y - move.start.y))
            is GetPieceResult -> if(objectiveCoordinates.piece.player != getStartPiecePlayer(move, board)) vectors.contains(Vector(move.end.x - move.start.x, move.end.y - move.start.y)) else false
            is OutOfBoundResult -> false
        }
    }

    private fun getStartPiecePlayer(move: MyMove, board: Board): Boolean{
        val piece = board.positions[move.start] ?: throw NoSuchElementException("The vector validator did not find the piece, match class should have caught this.")
        return piece.player;
    }

}

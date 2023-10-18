package validator

import Board
import EmptySquareResult
import MyMove
import OutOfBoundResult
import Vector
import GetPieceResult
import java.util.NoSuchElementException

data class TakeValidator(val direction: Vector, val distance: Int):Validator {
    override fun validate(move: MyMove, board: Board): Boolean {
        var currentCoordinates = move.start;
        for(i in 1..distance){
            when(val nextPosition = board.nextPosition(currentCoordinates, direction)){
                is EmptySquareResult -> currentCoordinates = nextPosition.coordinates
                is GetPieceResult -> return isEndPosition(nextPosition, move) && isEnemyPiece(nextPosition, move, board)
                is OutOfBoundResult -> return false
            }


        }
        return false
    }
    private fun isEnemyPiece(nextPosition: GetPieceResult, move: MyMove, board: Board): Boolean {
        val myPiece = board.positions[move.start] ?: throw NoSuchElementException("Take validator could not find the moving piece.")
        return myPiece.player != nextPosition.piece.player
    }

    private fun isEndPosition(nextPosition: GetPieceResult, move: MyMove): Boolean {
        return nextPosition.coordinates == move.end
    }
}

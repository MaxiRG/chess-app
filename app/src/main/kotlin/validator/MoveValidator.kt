package validator

import Board
import Coordinates
import EmptySquareResult
import GetPieceResult
import MyMove
import OutOfBoundResult
import Vector

data class MoveValidator(val direction: Vector, val distance: Int):Validator {
    override fun validate(move: MyMove, board: Board): Boolean {
        var currentCoordinates = move.start;
        for(i in 1..distance){
            when(val nextPosition = board.nextPosition(currentCoordinates, direction)){
                is EmptySquareResult -> currentCoordinates = if(isEndPosition(nextPosition.coordinates, move)) return true else (nextPosition.coordinates)
                is GetPieceResult -> return false
                is OutOfBoundResult -> return false
            }


        }
        return false
    }

    private fun isEndPosition(coordinates: Coordinates, move: MyMove): Boolean {
        return coordinates == move.end
    }

}

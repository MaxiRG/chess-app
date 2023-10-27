package validator

import Board
import Coordinates
import EmptySquareResult
import GetPieceResult
import MyMove
import OutOfBoundResult
import Vector
import piece.GetInvalidMoveResult
import piece.GetNormalMoveResult
import piece.GetPieceMoveResult
import java.util.NoSuchElementException

data class MoveValidator(val direction: Vector, val distance: Int, val canTake: Boolean, val canMove:Boolean):Validator {
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): GetPieceMoveResult {
        var currentCoordinates = move.start;
        var i=0
        while( i != distance){
            when(val nextPosition = board.nextPosition(currentCoordinates, direction)){
                is EmptySquareResult -> currentCoordinates = if(isEndPosition(nextPosition.coordinates, move) && canMove) return GetNormalMoveResult(move) else (nextPosition.coordinates)
                is GetPieceResult -> return if(canTake && isEndPosition(nextPosition.coordinates, move) && isEnemyPiece(nextPosition, move, board)) GetNormalMoveResult(move) else GetInvalidMoveResult(move)
                is OutOfBoundResult -> return GetInvalidMoveResult(move)
            }
            i++
        }
        return GetInvalidMoveResult(move)
    }

    private fun isEnemyPiece(nextPosition: GetPieceResult, move: MyMove, board: Board): Boolean {
        val myPiece = board.positions[move.start] ?: throw NoSuchElementException("Take validator could not find the moving piece.")
        return myPiece.player != nextPosition.piece.player
    }

    private fun isEndPosition(coordinates: Coordinates, move: MyMove): Boolean {
        return coordinates == move.end
    }

}

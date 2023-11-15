package edu.austral.dissis.checkers.validator

import edu.austral.dissis.commons.Board
import edu.austral.dissis.commons.Coordinates
import edu.austral.dissis.commons.EmptySquareResult
import edu.austral.dissis.commons.GetInvalidMoveResult
import edu.austral.dissis.commons.GetNormalMoveResult
import edu.austral.dissis.commons.GetPieceResult
import edu.austral.dissis.commons.MyMove
import edu.austral.dissis.commons.OutOfBoundResult
import edu.austral.dissis.commons.ValidatorResult
import edu.austral.dissis.commons.Vector
import edu.austral.dissis.commons.Validator
import kotlin.NoSuchElementException

class CheckersValidator(val direction: Vector, val distance: Int, val canTake: Boolean, val canMove:Boolean):
    Validator {
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): ValidatorResult {
        val takePosition = Coordinates(move.end.x - direction.x, move.end.y - direction.y)
        var currentCoordinates = move.start;
        var i=0
        while( i != distance){
            when(val nextPosition = board.nextPosition(currentCoordinates, direction)){
                is EmptySquareResult -> currentCoordinates = if(isEndPosition(nextPosition.coordinates, move) && canMove) return GetNormalMoveResult(
                    generateBoard(board, move, takePosition)
                ) else (nextPosition.coordinates)
                is GetPieceResult -> currentCoordinates  =if(canTake && isTakePosition(takePosition, nextPosition.coordinates, move) && isEnemyPiece(nextPosition, move, board)) nextPosition.coordinates else return GetInvalidMoveResult(move)
                is OutOfBoundResult -> return GetInvalidMoveResult(move)
            }
            i++
        }
        return GetInvalidMoveResult(move)
    }

    private fun isTakePosition(takePosition: Coordinates, coordinates: Coordinates, move: MyMove): Boolean {
        return coordinates == takePosition
    }

    private fun generateBoard(board: Board, move: MyMove, takePosition: Coordinates): Board {
        val piece = board.positions[move.start] ?: throw NoSuchElementException("The makeMove function could not find the piece.")
        val enemyPiece = board.positions[takePosition] ?: throw NoSuchElementException("The generateBoard function could not find the enemy piece")
        val newPositions = board.positions + (move.end to piece) - (move.start) - (takePosition)
        return board.copy(positions = newPositions, height = board.height, length = board.length)
    }

    private fun isEnemyPiece(nextPosition: GetPieceResult, move: MyMove, board: Board): Boolean {
        val myPiece = board.positions[move.start] ?: throw NoSuchElementException("Take validator could not find the moving piece.")
        return myPiece.player != nextPosition.piece.player
    }

    private fun isEndPosition(coordinates: Coordinates, move: MyMove): Boolean {
        return coordinates == move.end
    }
}
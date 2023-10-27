package validator

import Board
import Coordinates
import MyMove
import Vector
import piece.GetCastleMoveResult
import piece.GetInvalidMoveResult
import piece.GetPieceMoveResult

class CastleValidator:Validator {
    private val rightMoveValidator = MoveValidator(Vector.right, 3, false, true)
    private val leftMoveValidator = MoveValidator(Vector.left, 2, false, true)
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): GetPieceMoveResult {
        val leftIsValidMove = isValidMove(leftMoveValidator, move, board, moveHistory)
        val rightIsValidMove = isValidMove(rightMoveValidator, MyMove(move.player, move.start, Coordinates(move.end.x+1, move.end.y)), board, moveHistory)
        if(isCastleMove(move)){
            if(rightIsValidMove){
                return GetCastleMoveResult(move, MyMove(move.player, Coordinates(move.start.x+4, move.start.y), Coordinates(move.start.x+1, move.start.y)))
            }
            if(leftIsValidMove){
                return GetCastleMoveResult(move, MyMove(move.player, Coordinates(move.start.x-3, move.start.y), Coordinates(move.start.x-1, move.start.y)))
            }
        }
        return GetInvalidMoveResult(move)
    }

    private fun isValidMove(moveValidator: MoveValidator, move: MyMove, board: Board, moveHistory: List<Board>):Boolean{
        return when(moveValidator.validate(move, board, moveHistory)){
            is GetInvalidMoveResult -> false
            else -> true
        }
    }
    private fun isCastleMove(move: MyMove): Boolean {
        return listOf(Vector(2, 0), Vector(-2, 0)).contains(Vector(move.end.x - move.start.x, move.end.y - move.start.y))
    }

}
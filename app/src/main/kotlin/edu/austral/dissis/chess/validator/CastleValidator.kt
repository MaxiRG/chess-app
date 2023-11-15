package edu.austral.dissis.chess.validator

import edu.austral.dissis.commons.Board
import edu.austral.dissis.commons.Coordinates
import edu.austral.dissis.commons.MyMove
import edu.austral.dissis.commons.Vector
import edu.austral.dissis.commons.GetInvalidMoveResult
import edu.austral.dissis.commons.GetNormalMoveResult
import edu.austral.dissis.commons.ValidatorResult
import edu.austral.dissis.commons.Validator

class CastleValidator: Validator {
    private val rightMoveValidator = MoveValidator(Vector.right, 3, false, true)
    private val leftMoveValidator = MoveValidator(Vector.left, 2, false, true)
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): ValidatorResult {
        val leftIsValidMove = isValidMove(leftMoveValidator, move, board, moveHistory)
        val rightIsValidMove = isValidMove(rightMoveValidator, MyMove(move.player, move.start, Coordinates(move.end.x+1, move.end.y)), board, moveHistory)
        if(isCastleMove(move)){
            if(rightIsValidMove){
                return GetNormalMoveResult(generateBoard(board, move,MyMove(
                    move.player,
                    Coordinates(move.start.x + 4, move.start.y),
                    Coordinates(move.start.x + 1, move.start.y)
                )))
            }
            if(leftIsValidMove){
                return GetNormalMoveResult(generateBoard(board, move, MyMove(
                    move.player,
                    Coordinates(move.start.x - 3, move.start.y),
                    Coordinates(move.start.x - 1, move.start.y)
                )))
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

    private fun generateBoard(board: Board, kingMove: MyMove, rookMove: MyMove): Board {
        val king = board.positions[kingMove.start] ?: throw NoSuchElementException("The makeCastle function could not find the king.")
        val rook = board.positions[rookMove.start] ?: throw NoSuchElementException("The makeCastle function could not find the rook.")
        val newPositions = board.positions + (kingMove.end to king) + (rookMove.end to rook) - (kingMove.start) - (rookMove.start);
        return board.copy(positions = newPositions, height = board.height, length = board.length)
    }

}
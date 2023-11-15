package edu.austral.dissis.chess.validator

import edu.austral.dissis.commons.Board
import edu.austral.dissis.commons.Coordinates
import edu.austral.dissis.commons.MyMove
import edu.austral.dissis.commons.GetInvalidMoveResult
import edu.austral.dissis.commons.Piece
import edu.austral.dissis.commons.ValidatorResult
import edu.austral.dissis.commons.Validator

data class FirstMoveValidator(val myValidator: Validator): Validator {
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): ValidatorResult {
        val piece = board.positions[move.start] ?: throw NoSuchElementException("Could not find starting piece")
        if(isFirstMove(piece, moveHistory)){
            return myValidator.validate(move, board, moveHistory)
        }
        return GetInvalidMoveResult(move)
    }

    private fun isFirstMove(myPiece: Piece, moveHistory: List<Board>):Boolean {
        val startingCoordinates = startingPosition(myPiece, moveHistory);
        for (board in moveHistory) {
            if (board.positions[startingCoordinates].hashCode() != myPiece.hashCode()) {
                return false
            }
        }
        return true
    }

    private fun startingPosition(myPiece: Piece, moveHistory: List<Board>): Coordinates {
        for((coordinate, piece) in moveHistory[0].positions){
            if(piece.hashCode() == myPiece.hashCode()){
                return coordinate
            }
        }
        throw NoSuchElementException("Could not find the starting position of the piece.")
    }
}
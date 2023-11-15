package edu.austral.dissis.chess.validator

import edu.austral.dissis.commons.Board
import edu.austral.dissis.commons.EmptySquareResult
import edu.austral.dissis.commons.GetPieceResult
import edu.austral.dissis.commons.MyMove
import edu.austral.dissis.commons.OutOfBoundResult
import edu.austral.dissis.commons.Vector
import edu.austral.dissis.commons.GetInvalidMoveResult
import edu.austral.dissis.commons.GetNormalMoveResult
import edu.austral.dissis.commons.ValidatorResult
import edu.austral.dissis.commons.Validator

data class VectorValidator(val vectors: List<(Vector)>, val canTake: Boolean): Validator {
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): ValidatorResult {
        val isValidVector = vectors.contains(Vector(move.end.x - move.start.x, move.end.y - move.start.y))
        return when(val objectiveCoordinates = board.getPieceInPosition(move.end)){
            is EmptySquareResult -> if(isValidVector) GetNormalMoveResult(generateBoard(board, move)) else GetInvalidMoveResult(move)
            is GetPieceResult -> if(isEnemyPiece(objectiveCoordinates, move, board) && isValidVector && canTake) GetNormalMoveResult(
                generateBoard(board, move)
            ) else GetInvalidMoveResult(move)
            is OutOfBoundResult -> GetInvalidMoveResult(move)
        }
    }

    private fun isEnemyPiece(objectiveCoordinates: GetPieceResult, move: MyMove, board: Board) =
        objectiveCoordinates.piece.player != getStartPiecePlayer(move, board)

    private fun getStartPiecePlayer(move: MyMove, board: Board): Boolean{
        val piece = board.positions[move.start] ?: throw NoSuchElementException("The vector validator did not find the piece, match class should have caught this.")
        return piece.player;
    }

    private fun generateBoard(board: Board, move: MyMove): Board {
        val piece = board.positions[move.start] ?: throw java.util.NoSuchElementException("The makeMove function could not find the piece.")
        val newPositions = board.positions + (move.end to piece) - (move.start)
        return board.copy(positions = newPositions, height = board.height, length = board.length)
    }
}

package validator

import Board
import EmptySquareResult
import GetPieceResult
import MyMove
import OutOfBoundResult
import Vector
import piece.GetInvalidMoveResult
import piece.GetNormalMoveResult
import piece.GetPieceMoveResult

data class VectorValidator(val vectors: List<Vector>):Validator {
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): GetPieceMoveResult {
        val isValidVector = vectors.contains(Vector(move.end.x - move.start.x, move.end.y - move.start.y))
        return when(val objectiveCoordinates = board.getPieceInPosition(move.end)){
            is EmptySquareResult -> if(isValidVector) GetNormalMoveResult(move) else GetInvalidMoveResult(move)
            is GetPieceResult -> if(isEnemyPiece(objectiveCoordinates, move, board) && isValidVector) GetNormalMoveResult(move) else GetInvalidMoveResult(move)
            is OutOfBoundResult -> GetInvalidMoveResult(move)
        }
    }

    private fun isEnemyPiece(objectiveCoordinates: GetPieceResult, move: MyMove, board: Board) =
        objectiveCoordinates.piece.player != getStartPiecePlayer(move, board)

    private fun getStartPiecePlayer(move: MyMove, board: Board): Boolean{
        val piece = board.positions[move.start] ?: throw NoSuchElementException("The vector validator did not find the piece, match class should have caught this.")
        return piece.player;
    }

}

package piece

import Board
import Coordinates
import MyMove
import Vector
import edu.austral.dissis.chess.gui.Move
import validator.*

data class Piece(val id:Int, val validators: List<Validator>, val player: Boolean){
    fun isValidMove(move: MyMove, board: Board, moveHistory: List<Board>): GetPieceMoveResult {
        for(validator in validators){
            when(val validateResult = validator.validate(move, board, moveHistory)){
                is GetInvalidMoveResult -> continue
                else -> return validateResult
            }
        }
        return GetInvalidMoveResult(move)
    }
}
sealed interface GetPieceMoveResult
data class GetNormalMoveResult(val move: MyMove):GetPieceMoveResult
data class GetCastleMoveResult(val kingMove: MyMove, val rookMove: MyMove): GetPieceMoveResult
data class GetPromotionMoveResult(val move: MyMove): GetPieceMoveResult
data class GetInvalidMoveResult(val move: MyMove): GetPieceMoveResult
package validator

import Board
import Coordinates
import MyMove
import piece.*

class PromotionValidator(private val validator: Validator):Validator{
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): GetPieceMoveResult {
        val isValidMove = when(validator.validate(move, board, moveHistory)){
            is GetInvalidMoveResult -> false
            else -> true
        }
        if(isPromotionMove(move, board) && isValidMove) return GetPromotionMoveResult(move)
        return if(isValidMove) GetNormalMoveResult(move) else GetInvalidMoveResult(move)
    }

    private fun isPromotionMove(move: MyMove, board: Board):Boolean{
        var promotionCoordinates: List<Coordinates> = listOf()
        for(i in 0..board.length){
            promotionCoordinates = promotionCoordinates + Coordinates(i, board.height)
        }
        for(i in 0..board.length){
            promotionCoordinates = promotionCoordinates + Coordinates(i, 0)
        }
        return promotionCoordinates.contains(move.end);
    }

}
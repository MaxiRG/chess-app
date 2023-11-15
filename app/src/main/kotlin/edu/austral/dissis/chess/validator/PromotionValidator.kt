package edu.austral.dissis.chess.validator

import edu.austral.dissis.commons.*

class PromotionValidator(private val validator: Validator, private val promotionPieceValidators: List<Validator>): Validator {
    override fun validate(move: MyMove, board: Board, moveHistory: List<Board>): ValidatorResult {
        val useBoard = when(val isValidMove = validator.validate(move, board, moveHistory)){
            is GetInvalidMoveResult -> return GetInvalidMoveResult(move);
            is GetNormalMoveResult -> isValidMove.board
        }
        return if(isPromotionMove(move, useBoard)) GetNormalMoveResult(generatePromotionBoard(useBoard, move)) else GetNormalMoveResult(useBoard)
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

    private fun generatePromotionBoard(board: Board, move: MyMove): Board {
        val fromPiece = board.positions[move.end] ?: throw NoSuchElementException("From piece not found")
        val piece = getPiecePick(fromPiece.id, fromPiece.player);
        val newPositions = board.positions  - (move.end) + (move.end to piece)
        return board.copy(positions = newPositions, height = board.height, length = board.length)
    }

    private fun getPiecePick(id: Int, turn: Boolean): Piece {
        return Piece(id, promotionPieceValidators, turn);
    }

}
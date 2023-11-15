package edu.austral.dissis.commons

class BoardGenerator {
    fun generateBoard(piece: Piece, move: MyMove, board: Board, moveHistory: List<Board>): ValidatorResult {
        val validators = piece.validators;
        for(validator in validators){
            when(val validateResult = validator.validate(move, board, moveHistory)){
                is GetInvalidMoveResult -> continue
                else -> return validateResult
            }
        }
        return GetInvalidMoveResult(move)
    }

}

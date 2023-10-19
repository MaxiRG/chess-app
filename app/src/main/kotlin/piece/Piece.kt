package piece

import Board
import Coordinates
import MyMove
import Vector
import validator.MoveValidator
import validator.TakeValidator
import validator.Validator
import validator.VectorValidator

data class Piece(val id:Int, val pieceType: PieceType, val player: Boolean){
    fun isValidMove(move: MyMove, board: Board, moveHistory: List<Board>): GetIsValidMoveResult {
        return when(pieceType){
            is King -> getResultKing(move, board, moveHistory);
            is Pawn -> getResultPawn(move, board, moveHistory);
            else -> validateMove(pieceType.validators, move, board);
        }

    }

    private fun getResultKing(move: MyMove, board: Board, moveHistory: List<Board>): GetIsValidMoveResult {
        val startingPosition = startingPosition(moveHistory)
        pieceType as King
        if(player){
            if(isFirstMove(moveHistory) && isCastleMove(move)){
                if(pieceType.rightCastleValidator.validate(move, board)){
                    return GetCastleMoveResult(move, MyMove(player, Coordinates(startingPosition.x +3, startingPosition.y), Coordinates(startingPosition.x +1, startingPosition.y)))
                } else if(pieceType.leftCastleValidator.validate(move, board)){
                    return GetCastleMoveResult(move, MyMove(player, Coordinates(startingPosition.x -4, startingPosition.y), Coordinates(startingPosition.x -1, startingPosition.y)))
                } else {
                    return GetInvalidMoveResult(move);
                }

            } else {
                return validateMove(pieceType.validators, move, board);
            }
        } else {
            if(isFirstMove(moveHistory) && isCastleMove(move)){
                if(pieceType.rightCastleValidator.validate(move, board)){
                    return GetCastleMoveResult(move, MyMove(player, Coordinates(startingPosition.x +3, startingPosition.y), Coordinates(startingPosition.x +1, startingPosition.y)))
                } else if(pieceType.leftCastleValidator.validate(move, board)){
                    return GetCastleMoveResult(move, MyMove(player, Coordinates(startingPosition.x -3, startingPosition.y), Coordinates(startingPosition.x -1, startingPosition.y)))
                } else {
                    return GetInvalidMoveResult(move);
                }
            } else {
                return validateMove(pieceType.validators, move, board);
            }
        }
    }

    private fun isCastleMove(move: MyMove): Boolean {
        return listOf(Coordinates(2,0), Coordinates(6, 0), Coordinates(2, 7), Coordinates(6, 7)).contains(move.end)
    }

    private fun validateMove(validators: List<Validator>, move: MyMove, board: Board): GetIsValidMoveResult{
        for(validator in validators){
            if(validator.validate(move, board)){
                return GetNormalMoveResult(move)
            }
        }
        return GetInvalidMoveResult(move)
    }

    private fun getResultPawn(move: MyMove, board: Board, moveHistory: List<Board>): GetIsValidMoveResult {
        fun validateMovePawn(validators: List<Validator>, firstMoveValidator: Validator, moveHistory: List<Board>, move: MyMove, board: Board): GetIsValidMoveResult {
            val validateMoveResult = validateMove(validators, move, board);
            return if (isFirstMove(moveHistory) && firstMoveValidator.validate(move, board)) {
                GetNormalMoveResult(move)
            } else if(isPromotionMove(move, board)){
                when(validateMoveResult){
                    is GetCastleMoveResult -> throw IllegalStateException("This function should never return this.")
                    is GetInvalidMoveResult -> return GetInvalidMoveResult(move)
                    is GetNormalMoveResult -> return GetPromotionMoveResult(move)
                    is GetPromotionMoveResult -> throw IllegalStateException("This function should never return this.")
                }
            } else {
                return validateMoveResult
            }
        }
        pieceType as Pawn
        return if (player) {
            validateMovePawn(pieceType.validators, pieceType.firstMoveValidator, moveHistory, move, board);
        } else {
            validateMovePawn(pieceType.blackValidators, pieceType.blackFirstMoveValidator, moveHistory, move, board);
        }

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


    private fun isFirstMove(moveHistory: List<Board>):Boolean {
        val startingCoordinates = startingPosition(moveHistory);
        for (board in moveHistory) {
            if (board.positions[startingCoordinates].hashCode() != this.hashCode()) {
                return false
            }
        }
        return true
    }

    private fun startingPosition(moveHistory: List<Board>):Coordinates{
        for((coordinate, piece) in moveHistory[0].positions){
            if(piece.hashCode() == this.hashCode()){
                return coordinate
            }
        }
        throw NoSuchElementException("Could not find the starting position of the piece.")
    }
}

sealed interface GetIsValidMoveResult
data class GetNormalMoveResult(val move: MyMove):GetIsValidMoveResult
data class GetCastleMoveResult(val kingMove: MyMove, val rookMove: MyMove): GetIsValidMoveResult
data class GetPromotionMoveResult(val move: MyMove): GetIsValidMoveResult
data class GetInvalidMoveResult(val move: MyMove): GetIsValidMoveResult


sealed interface PieceType {
    val validators: List<Validator>
}
data class Pawn(override
                val validators: List<Validator> = listOf(TakeValidator(Vector.upRight,1), TakeValidator(Vector.upLeft,1), MoveValidator(Vector.up, 1)),
                val blackValidators: List<Validator> = listOf(TakeValidator(Vector.downRight,1), TakeValidator(Vector.downLeft,1), MoveValidator(Vector.down, 1)),
                val firstMoveValidator: MoveValidator = MoveValidator(Vector.up, 2),
                val blackFirstMoveValidator: MoveValidator = MoveValidator(Vector.down, 2)
): PieceType

data class King(override val validators: List<Validator> = listOf(VectorValidator(listOf(Vector.up, Vector.down, Vector.left, Vector.right, Vector.upLeft, Vector.upRight, Vector.downLeft, Vector.downRight))),
                val leftCastleValidator: Validator = MoveValidator((Vector.left), 3),
                val rightCastleValidator: Validator = MoveValidator(Vector.right, 2),
                val blackLeftCastleValidator: Validator = MoveValidator(Vector.left, 2),
                val blackRightCastleValidator: Validator = MoveValidator(Vector.right, 3)
): PieceType
data class Knight(override val validators: List<Validator> = listOf(
    VectorValidator(
        listOf(
            Vector(1,2), Vector(-1, 2), Vector(2, 1), Vector(2, -1), Vector(-2, 1), Vector(-2, -1), Vector(1, -2), Vector(-1, -2)
        )
    )
)):PieceType
data class Queen(override val validators: List<Validator> = listOf(
    TakeValidator(Vector.up, Int.MAX_VALUE), TakeValidator(Vector.down, Int.MAX_VALUE), TakeValidator(Vector.left, Int.MAX_VALUE), TakeValidator(Vector.right, Int.MAX_VALUE), TakeValidator(Vector.downLeft, Int.MAX_VALUE), TakeValidator(Vector.downRight, Int.MAX_VALUE), TakeValidator(Vector.upLeft, Int.MAX_VALUE), TakeValidator(Vector.upRight, Int.MAX_VALUE),
    MoveValidator(Vector.up, Int.MAX_VALUE), MoveValidator(Vector.down, Int.MAX_VALUE), MoveValidator(Vector.left, Int.MAX_VALUE), MoveValidator(Vector.right, Int.MAX_VALUE), MoveValidator(Vector.downLeft, Int.MAX_VALUE), MoveValidator(Vector.downRight, Int.MAX_VALUE), MoveValidator(Vector.upLeft, Int.MAX_VALUE), MoveValidator(Vector.upRight, Int.MAX_VALUE)
)
):PieceType
data class Rook(override val validators: List<Validator> = listOf(
    TakeValidator(Vector.up, Int.MAX_VALUE), TakeValidator(Vector.down, Int.MAX_VALUE), TakeValidator(Vector.left, Int.MAX_VALUE), TakeValidator(Vector.right, Int.MAX_VALUE),
    MoveValidator(Vector.up, Int.MAX_VALUE), MoveValidator(Vector.down, Int.MAX_VALUE), MoveValidator(Vector.left, Int.MAX_VALUE), MoveValidator(Vector.right, Int.MAX_VALUE)
)
):PieceType
data class Bishop(override val validators: List<Validator> = listOf(
    TakeValidator(Vector.downLeft, Int.MAX_VALUE), TakeValidator(Vector.downRight, Int.MAX_VALUE), TakeValidator(Vector.upLeft, Int.MAX_VALUE), TakeValidator(Vector.upRight, Int.MAX_VALUE),
    MoveValidator(Vector.downLeft, Int.MAX_VALUE), MoveValidator(Vector.downRight, Int.MAX_VALUE), MoveValidator(Vector.upLeft, Int.MAX_VALUE), MoveValidator(Vector.upRight, Int.MAX_VALUE)
)
):PieceType

data class Generic(override val validators: List<Validator>):PieceType
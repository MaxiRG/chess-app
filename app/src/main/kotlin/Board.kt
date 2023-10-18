import piece.Piece

data class Board(val positions: Map<Coordinates, Piece>, val height: Int, val length: Int){
    fun getPieceInPosition(coordinates: Coordinates): GetPieceInPositionResult {
        if(isOutOfBounds(coordinates)) {
            return OutOfBoundResult("The $coordinates are out of bounds")
        }
        val piece = positions[coordinates];
        return if(piece != null){
            GetPieceResult(piece, coordinates);
        } else {
            EmptySquareResult(coordinates);
        }
    }
    private fun isOutOfBounds(coordinates: Coordinates) =
        coordinates.x > length || coordinates.x < 0 || coordinates.y > height || coordinates.y < 0

    fun nextPosition(coordinates: Coordinates, vector: Vector):GetPieceInPositionResult{
        return getPieceInPosition(Coordinates(coordinates.x + vector.x, coordinates.y + vector.y))
    }
}


sealed interface GetPieceInPositionResult
data class GetPieceResult(val piece: Piece, val coordinates: Coordinates): GetPieceInPositionResult
data class OutOfBoundResult(val string: String): GetPieceInPositionResult
data class EmptySquareResult(val coordinates: Coordinates): GetPieceInPositionResult
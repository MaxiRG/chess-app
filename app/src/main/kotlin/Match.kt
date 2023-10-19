import piece.*
import rules.Rules
import kotlin.NoSuchElementException

data class Match(val rules: Rules, val playerTurn: Boolean, val board: Board, val moveHistory: List<Board>, val active: Boolean) {
    fun movePiece(move: MyMove): GetPlayResult {
        if(!isPlayerTurn(move)) return GetInvalidPlayResult(this, move);

        val piece: Piece;
        when(val coordinatesResult = board.getPieceInPosition(move.start)){
            is EmptySquareResult -> return GetInvalidPlayResult(this, move)
            is GetPieceResult -> piece = coordinatesResult.piece
            is OutOfBoundResult -> return GetInvalidPlayResult(this, move)
        }

        if(!isPLayerPiece(piece)) return GetInvalidPlayResult(this, move);

        val newBoard: Board = when(val isValidMove = piece.isValidMove(move, board, moveHistory)){
            is GetCastleMoveResult -> makeCastle(isValidMove.kingMove, isValidMove.rookMove);
            is GetInvalidMoveResult -> return GetInvalidPlayResult(this, move)
            is GetNormalMoveResult -> makeMove(move);
            is GetPromotionMoveResult -> makePromotion(move);
        }

        val newHistory = generateNewHistory(newBoard, moveHistory);
        val newActive = returnWinner() == -1;

        if(rules.isInCheck(newBoard, playerTurn, newHistory)){
            return GetInvalidPlayResult(this, move);
        }

        val newMatch = copy(rules = rules, playerTurn = !playerTurn, board = newBoard, moveHistory = newHistory, active = newActive);
        if(rules.checkWon(newMatch)){
            return GetWonPlayResult(this, playerTurn)
        }
        return GetValidPlayResult(newMatch);
    }

    private fun isPLayerPiece(piece: Piece): Boolean {
        return piece.player == playerTurn;
    }

    fun makePromotion(move: MyMove): Board {
        val fromPiece = board.positions[move.start] ?: throw NoSuchElementException("From piece not found")
        val piece = getPiecePick(fromPiece.id);
        val newPositions = board.positions + (move.end to piece) - (move.start)
        return board.copy(positions = newPositions, height = board.height, length = board.length)
    }

    private fun getPiecePick(id: Int): Piece {
        return Piece(id, Queen(), playerTurn);
    }

    fun makeMove(move: MyMove): Board {
        val piece = board.positions[move.start] ?: throw NoSuchElementException("The makeMove function could not find the piece.")
        val newPositions = board.positions + (move.end to piece) - (move.start)
        return board.copy(positions = newPositions, height = board.height, length = board.length)
    }

    fun makeCastle(kingMove: MyMove, rookMove: MyMove): Board {
        val king = board.positions[kingMove.start] ?: throw NoSuchElementException("The makeCastle function could not find the king.")
        val rook = board.positions[rookMove.start] ?: throw NoSuchElementException("The makeCastle function could not find the rook.")
        val newPositions = board.positions + (kingMove.end to king) + (rookMove.end to rook) - (kingMove.start) - (rookMove.start);
        return board.copy(positions = newPositions, height = board.height, length = board.length)
    }

    private fun isPlayerTurn(move:MyMove): Boolean {
        return move.player == playerTurn
    }

    private fun generateNewHistory(newBoard: Board, moveHistory: List<Board>): List<Board> {
        return moveHistory.plus(newBoard);
    }

    private fun returnWinner(): Int {
        return -1;
    }
}

sealed interface GetPlayResult
data class GetValidPlayResult(val match: Match): GetPlayResult
data class GetInvalidPlayResult(val match: Match, val move: MyMove): GetPlayResult
data class GetWonPlayResult(val match: Match, val player:Boolean): GetPlayResult
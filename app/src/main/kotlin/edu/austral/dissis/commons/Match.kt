package edu.austral.dissis.commons

data class Match(val rules: Rules, val playerTurn: Boolean, val board: Board, val moveHistory: List<Board>, val active: Boolean) {
    private val boardGenerator =  BoardGenerator();
    fun movePiece(move: MyMove): GetPlayResult {
        if(!isPlayerTurn(move)) return GetInvalidPlayResult(this, move);

        val piece: Piece;
        when(val coordinatesResult = board.getPieceInPosition(move.start)){
            is EmptySquareResult -> return GetInvalidPlayResult(this, move)
            is GetPieceResult -> piece = coordinatesResult.piece
            is OutOfBoundResult -> return GetInvalidPlayResult(this, move)
        }

        if(!isPLayerPiece(piece)) return GetInvalidPlayResult(this, move);

        val newBoard: Board = when(val isValidMove = boardGenerator.generateBoard(piece, move, board, moveHistory)){
            is GetInvalidMoveResult -> return GetInvalidPlayResult(this, move)
            is GetNormalMoveResult -> isValidMove.board;

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


    private fun isPlayerTurn(move: MyMove): Boolean {
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
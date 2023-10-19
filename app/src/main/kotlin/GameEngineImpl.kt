import edu.austral.dissis.chess.gui.*
import edu.austral.dissis.chess.gui.Move
import piece.*
import rules.ClassicRules

class GameEngineImpl:GameEngine {
    val rules = ClassicRules()
    val startingBoard = rules.startingPositions
    val startingBoardList = convertMap(startingBoard.positions)
    var match = Match(ClassicRules(), true,startingBoard,listOf(startingBoard), true)
    override fun applyMove(move: Move): MoveResult {
        return when(val moveResult = match.movePiece(MyMove(match.playerTurn,Coordinates(move.from.column-1, move.from.row-1), Coordinates(move.to.column-1, move.to.row-1)))){
            is GetInvalidPlayResult -> InvalidMove("Invalid move!")
            is GetValidPlayResult -> {
                match = moveResult.match
                val pieces = convertMap(match.board.positions)
                NewGameState(pieces,if(match.playerTurn) PlayerColor.WHITE else PlayerColor.BLACK)
            }

            is GetWonPlayResult -> GameOver(if(moveResult.player) PlayerColor.WHITE else PlayerColor.BLACK)
        }

    }

    private fun convertMap(map: Map<Coordinates, Piece>):List<ChessPiece>{
        var list:List<ChessPiece> = listOf()
        var color:PlayerColor
        for((key, value) in map){
            color = if(value.player) PlayerColor.WHITE else PlayerColor.BLACK
            list = list + ChessPiece(value.hashCode().toString(), color, Position(key.y+1,key.x+1), getPieceTypeString(value.pieceType))
        }
        return list
    }

    private fun getPieceTypeString(pieceType: PieceType):String{
        return when(pieceType){
            is Bishop -> "bishop"
            is King -> "king"
            is Knight -> "knight"
            is Pawn -> "pawn"
            is Queen -> "queen"
            is Rook -> "rook"
            is Generic -> "archbishop"
        }
    }

    override fun init(): InitialState {
        return InitialState(BoardSize(8,8), startingBoardList, PlayerColor.WHITE)
    }
}
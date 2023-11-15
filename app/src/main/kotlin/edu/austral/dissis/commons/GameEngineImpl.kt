package edu.austral.dissis.commons

import edu.austral.dissis.chess.gui.*
import edu.austral.dissis.chess.gui.Move
import edu.austral.dissis.checkers.rules.CheckersRules
import edu.austral.dissis.checkers.rules.CheckersRules.Companion.BCHECKER
import edu.austral.dissis.checkers.rules.CheckersRules.Companion.QUEEN_CHECKER
import edu.austral.dissis.checkers.rules.CheckersRules.Companion.RCHECKER
import edu.austral.dissis.chess.rules.ClassicRules
import edu.austral.dissis.chess.rules.ClassicRules.Companion.BISHOP
import edu.austral.dissis.chess.rules.ClassicRules.Companion.BPAWN
import edu.austral.dissis.chess.rules.ClassicRules.Companion.KING
import edu.austral.dissis.chess.rules.ClassicRules.Companion.KNIGHT
import edu.austral.dissis.chess.rules.ClassicRules.Companion.PAWN
import edu.austral.dissis.chess.rules.ClassicRules.Companion.QUEEN
import edu.austral.dissis.chess.rules.ClassicRules.Companion.ROOK

class GameEngineImpl(rules: Rules):GameEngine {
    val startingBoard = rules.startingPositions
    val startingBoardList = convertMap(startingBoard.positions)
    var match = Match(rules, true,startingBoard,listOf(startingBoard), true)

    companion object {
        fun withChessRules(): GameEngineImpl{
            return GameEngineImpl(ClassicRules());
        }

        fun withCheckersRules(): GameEngineImpl {
            return GameEngineImpl(CheckersRules())
        }
    }
    override fun applyMove(move: Move): MoveResult {
        return when(val moveResult = match.movePiece(
            MyMove(match.playerTurn,
                Coordinates(move.from.column-1, move.from.row-1), Coordinates(move.to.column-1, move.to.row-1)
            )
        )){
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
            list = list + ChessPiece(value.id.toString(), color, Position(key.y+1,key.x+1), getPieceTypeString(value.validators))
        }
        return list
    }

    private fun getPieceTypeString(validators: List<Validator>):String{
        return when(validators){
            KING -> "king"
            ROOK -> "rook"
            BISHOP -> "bishop"
            KNIGHT -> "knight"
            PAWN -> "pawn"
            BPAWN -> "pawn"
            QUEEN -> "queen"
            RCHECKER -> "pawn"
            BCHECKER -> "pawn"
            QUEEN_CHECKER -> "queen"
            else -> "archbishop"
        }
    }

    override fun init(): InitialState {
        return InitialState(BoardSize(8,8), startingBoardList, PlayerColor.WHITE)
    }
}
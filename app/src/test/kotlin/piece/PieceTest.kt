package piece

import edu.austral.dissis.commons.Board
import edu.austral.dissis.commons.Coordinates
import GetCastleMoveResult
import edu.austral.dissis.commons.GetInvalidMoveResult
import edu.austral.dissis.commons.GetInvalidPlayResult
import edu.austral.dissis.commons.GetNormalMoveResult
import GetPromotionMoveResult
import edu.austral.dissis.commons.GetValidPlayResult
import edu.austral.dissis.commons.GetWonPlayResult
import edu.austral.dissis.commons.Match
import edu.austral.dissis.commons.MyMove
import edu.austral.dissis.commons.Piece
import edu.austral.dissis.chess.rules.ClassicRules
import edu.austral.dissis.chess.rules.ClassicRules.Companion.BISHOP
import edu.austral.dissis.chess.rules.ClassicRules.Companion.KING
import edu.austral.dissis.chess.rules.ClassicRules.Companion.KNIGHT
import edu.austral.dissis.chess.rules.ClassicRules.Companion.PAWN
import edu.austral.dissis.chess.rules.ClassicRules.Companion.QUEEN
import edu.austral.dissis.chess.rules.ClassicRules.Companion.ROOK
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PieceTest{
    private fun parseChessBoardFromFile(filePath: String, piece: Piece, startingPosition: Coordinates):Boolean {
        val lines = mutableListOf<String>()
        try {
            val file = File(filePath)
            if (file.exists()) {
                file.useLines { lines.addAll(it) }
            } else {
                throw FileNotFoundException("File not found: $filePath")
            }
        } catch (e: IOException) {
            println("Error reading the file: ${e.message}")
            return false
        }
        if (lines.size != 8) {
            println("Invalid chess board format. The file should contain exactly 8 lines. Currently it is ${lines.size}")
            return false
        }
        for (y in 0 until 8) {
            val line = lines[y]
            if (line.length != 15) {
                println("Invalid chess board format. Each line should have exactly 15 characters. Line number $y has ${line.length}")
                return false
            }
            var spacesCounter = 0
            for (x in 0 until 15) {
                val character = line[x]
                if(character != ' '){
                    val coordinate = Coordinates(x - spacesCounter, y)
                    if(!pieceAnswersCorrectly(character, coordinate, piece, startingPosition)) {
                        print("Piece answered incorrectly for $coordinate")
                        return false
                    }
                } else {
                    spacesCounter++
                }
            }
        }
        return true
    }

    private fun pieceAnswersCorrectly(character: Char, coordinate: Coordinates, piece: Piece, startingPosition: Coordinates): Boolean {
        val board = Board(mapOf(startingPosition to piece),7, 7)
        val history = listOf(board);
        when(character){
            'x' -> {when(val isValidMoveResult = piece.isValidMove(MyMove(piece.player, startingPosition, coordinate),board, history)){
                is GetCastleMoveResult -> throw IllegalStateException("Should not return castle move while trying to move to $coordinate")
                is GetInvalidMoveResult -> throw Exception("Piece in position ${startingPosition} should be able to move to $coordinate")
                is GetNormalMoveResult -> return true
                is GetPromotionMoveResult -> throw IllegalStateException("Should not return promotion move while trying to move to $coordinate")
            } }
            '-' -> {when(val isValidMoveResult = piece.isValidMove(MyMove(piece.player, startingPosition, coordinate),board, history)){
                is GetCastleMoveResult -> throw IllegalStateException("Should not return castle move while trying to move to $coordinate")
                is GetInvalidMoveResult -> return true
                is GetNormalMoveResult -> throw Exception("Piece in position ${startingPosition} should NOT be able to move to $coordinate")
                is GetPromotionMoveResult -> throw IllegalStateException("Should not return promotion move while trying to move to $coordinate")
            } }
            'p' -> if(coordinate != startingPosition) throw Exception("The piece was not located correctly, should be in $coordinate") else return true
        }
        return false
    }

    @Test
    fun testPawnMove() {
        val pawn = Piece(1, PAWN, true)
        assertTrue(parseChessBoardFromFile("src/test/resources/pawnMove.txt", pawn, Coordinates(0, 0)))

    }

    @Test
    fun testQueenMove(){
        val queen = Piece(1, QUEEN, true)
        assertTrue(parseChessBoardFromFile("src/test/resources/queenMove.txt", queen, Coordinates(3, 4)))
    }

    @Test
    fun testRookMove(){
        val rook = Piece(1, ROOK, true)
        assertTrue(parseChessBoardFromFile("src/test/resources/rookMove.txt", rook, Coordinates(3, 4)))
    }

    @Test
    fun testKnightMove(){
        val knight = Piece(1, KNIGHT, true)
        assertTrue(parseChessBoardFromFile("src/test/resources/knightMove.txt", knight, Coordinates(3, 4)))
    }

    @Test
    fun testBishopMove(){
        val bishop = Piece(1, BISHOP, true)
        assertTrue(parseChessBoardFromFile("src/test/resources/bishopMove.txt", bishop, Coordinates(3, 4)))
    }

    @Test
    fun testKingMove(){
        val king = Piece(1, KING, true)
        assertTrue(parseChessBoardFromFile("src/test/resources/kingMove.txt", king, Coordinates(3, 4)))
    }

    @Test
    fun testValidWhiteKingShortCastle(){
        val map = mapOf(
            Coordinates(3, 0) to Piece(13, KING, true), Coordinates(7,0) to Piece(9, ROOK, true), Coordinates(6, 7) to Piece(
            1,
            KING,
            false
        )
        )
        val board = Board(map, 7, 7)
        val match = Match(ClassicRules(), true, board, listOf(board), true)
        val resultMap: Map<Coordinates, Piece> =
            when(val resultGame = match.movePiece(MyMove(true, Coordinates(3, 0), Coordinates(5,0)))) {
                is GetInvalidPlayResult -> map;
                is GetValidPlayResult -> resultGame.match.board.positions;
                is GetWonPlayResult -> resultGame.match.board.positions
            }
        val correctMap = mapOf(
            Coordinates(5, 0) to Piece(13, KING, true), Coordinates(4,0) to Piece(9, ROOK, true), Coordinates(6, 7) to Piece(
            1,
            KING,
            false
        )
        )
        assertEquals(correctMap, resultMap)
    }

    @Test
    fun testValidWhiteKingLongCastle(){
        val map = mapOf(
            Coordinates(7, 7) to Piece(1, KING, false), Coordinates(3, 0) to Piece(13, KING, true), Coordinates(0,0) to Piece(
            19,
            ROOK,
            true
        )
        )
        val board = Board(map, 7, 7)
        val match = Match(ClassicRules(), true, board, listOf(board), true)
        val resultMap: Map<Coordinates, Piece> =
            when(val resultGame = match.movePiece(MyMove(true, Coordinates(3, 0), Coordinates(1,0)))) {
                is GetInvalidPlayResult -> map;
                is GetValidPlayResult -> resultGame.match.board.positions;
                is GetWonPlayResult -> resultGame.match.board.positions
            }
        val correctMap = mapOf(
            Coordinates(7, 7) to Piece(1, KING, false), Coordinates(1, 0) to Piece(13, KING, true), Coordinates(2,0) to Piece(
            19,
            ROOK,
            true
        )
        )
        assertEquals(correctMap, resultMap)
    }

    @Test
    fun testValidBlackKingLongCastle(){
        val map = mapOf(
            Coordinates(0, 0) to Piece(1, KING, false), Coordinates(3, 7) to Piece(1, KING, true), Coordinates(7,7) to Piece(
            1,
            ROOK,
            true
        )
        )
        val board = Board(map, 7, 7)
        val match = Match(ClassicRules(), true, board, listOf(board), true)
        val resultMap: Map<Coordinates, Piece> =
            when(val resultGame = match.movePiece(MyMove(true, Coordinates(3, 7), Coordinates(5,7)))) {
                is GetInvalidPlayResult -> map;
                is GetValidPlayResult -> resultGame.match.board.positions;
                is GetWonPlayResult -> resultGame.match.board.positions
            }
        val correctMap = mapOf(
            Coordinates(0, 0) to Piece(1, KING, false), Coordinates(5, 7) to Piece(1, KING, true), Coordinates(4,7) to Piece(
            1,
            ROOK,
            true
        )
        )
        assertEquals(correctMap, resultMap)
    }

    @Test
    fun testValidBlackKingShortCastle(){
        val map = mapOf(
            Coordinates(7, 7) to Piece(1, KING, false), Coordinates(3, 7) to Piece(1, KING, true), Coordinates(0,7) to Piece(
            1,
            ROOK,
            true
        )
        )
        val board = Board(map, 7, 7)
        val match = Match(ClassicRules(), true, board, listOf(board), true)
        val resultMap: Map<Coordinates, Piece> =
            when(val resultGame = match.movePiece(MyMove(true, Coordinates(3, 7), Coordinates(1,7)))) {
                is GetInvalidPlayResult -> map;
                is GetValidPlayResult -> resultGame.match.board.positions;
                is GetWonPlayResult -> resultGame.match.board.positions
            }
        val correctMap = mapOf(
            Coordinates(7, 7) to Piece(1, KING, false), Coordinates(1, 7) to Piece(1, KING, true), Coordinates(2,7) to Piece(
            1,
            ROOK,
            true
        )
        )
        assertEquals(correctMap, resultMap)
    }
}
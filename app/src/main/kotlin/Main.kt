import piece.*
import rules.ClassicRules

fun main(args: Array<String>) {
    val rules = ClassicRules()
    var match = Match(rules, true, rules.startingPositions, listOf(rules.startingPositions), true)
    var turn = true;
    displayChessboard(match.board.positions)
    while(true) {
        val move = promptForMove(turn);
        when (val getPlayResult = match.movePiece(move)) {
            is GetInvalidPlayResult -> println("Invalid move")
            is GetValidPlayResult -> {
                match = getPlayResult.match
                turn = !turn
                displayChessboard(match.board.positions)
            }

        }
    }
}

fun displayChessboard(chessboard: Map<Coordinates, Piece>) {
    val boardSize = 8 // Assuming a 8x8 chessboard, adjust as needed

    for (y in 0 until boardSize) {
        for (x in 0 until boardSize) {
            val currentCoord = Coordinates(x, y)
            val piece = chessboard[currentCoord]

            val pieceSymbol = when (piece?.pieceType) {
                is Bishop -> "B"
                is King -> "K"
                is Knight -> "N"
                is Pawn -> "P"
                is Queen -> "Q"
                is Rook -> "R"
                else -> " " // Empty square
            }

            print("$pieceSymbol ")
        }
        println() // Move to the next row
    }
}

fun promptForMove(player: Boolean): MyMove {
    val piecePlayer = if (player) "White" else "Black"

    println("$piecePlayer's Turn")

    // Prompt for the starting coordinates
    print("Enter starting coordinates (x y): ")
    val startInput = readLine()
    val startCoordinates = parseCoordinates(startInput)

    // Prompt for the ending coordinates
    print("Enter ending coordinates (x y): ")
    val endInput = readLine()
    val endCoordinates = parseCoordinates(endInput)

    return MyMove(player, startCoordinates, endCoordinates)
}
fun parseCoordinates(input: String?): Coordinates {
    val coordinates = input?.split(' ')
    if (coordinates?.size == 2) {
        val x = coordinates[0].toIntOrNull()
        val y = coordinates[1].toIntOrNull()
        if (x != null && y != null) {
            return Coordinates(x, y)
        }
    }
    println("Invalid coordinates. Please enter valid coordinates.")
    return parseCoordinates(readLine())
}

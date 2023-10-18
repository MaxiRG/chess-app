package validator

import Board
import MyMove

interface Validator {
    fun validate(move: MyMove, board: Board):Boolean
}
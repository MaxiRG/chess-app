package edu.austral.dissis.commons

interface Validator {
    fun validate(move: MyMove, board: Board, moveHistory: List<Board>): ValidatorResult
}
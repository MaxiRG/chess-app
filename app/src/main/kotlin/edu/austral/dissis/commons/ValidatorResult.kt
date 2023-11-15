package edu.austral.dissis.commons

sealed interface ValidatorResult
data class GetNormalMoveResult(val board: Board): ValidatorResult
data class GetInvalidMoveResult(val move: MyMove): ValidatorResult
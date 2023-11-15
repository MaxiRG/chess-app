package edu.austral.dissis.commons

import edu.austral.dissis.commons.Validator

data class Piece(val id:Int, val validators: List<Validator>, val player: Boolean){
}

package edu.austral.dissis.commons

sealed interface Pair
data class Coordinates(val x:Int, val y:Int): Pair
data class Vector(val x:Int, val y:Int): Pair {
    companion object{
        val up = Vector(0,1)
        val down = Vector(0, -1)
        val left = Vector(-1, 0)
        val right = Vector(1, 0)
        val upLeft = Vector(-1,1)
        val upRight = Vector(1, 1)
        val downLeft = Vector(-1, -1)
        val downRight = Vector(1, -1)
    }
}

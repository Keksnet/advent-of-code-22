package de.neo.adventofcode.days.day09

import de.neo.adventofcode.days.Day09
import kotlin.math.absoluteValue

class Vec2D(var x: Int, var y: Int) {

    fun normalize(): Int {
        val highestValue = x.absoluteValue.coerceAtMost(y.absoluteValue)
        x /= highestValue
        y /= highestValue
        return highestValue
    }

    // This method applies a kind of mask to the vector.
    // Every value is zero or one or minus one after this operation.
    fun mask() {
        if (x > 0) x = 1
        else if (x < 0) x = -1

        if (y > 0) y = 1
        else if (y < 0) y = -1
    }

    fun getDirection(): Day09.Direction {
        return if (x == 0) {
            if (y > 0) Day09.Direction.UP
            else if (y < 0) Day09.Direction.DOWN
            else Day09.Direction.UNKNOWN
        } else if (y == 0) {
            if (x > 0) Day09.Direction.RIGHT
            else if (x < 0) Day09.Direction.LEFT
            else Day09.Direction.UNKNOWN
        } else Day09.Direction.UNKNOWN
    }

}
package de.neo.adventofcode.days

import de.neo.adventofcode.days.day09.Vec2D
import java.awt.Point
import kotlin.math.absoluteValue

class Day09 : IDay {

    enum class Direction(val xMod: Int, val yMod: Int) {
        UNKNOWN(0, 0),
        RIGHT(1, 0),
        UP(0, 1),
        LEFT(-1, 0),
        DOWN(0, -1);

        companion object {
            fun getDirection(direction: String): Direction {
                return when (direction) {
                    "R" -> RIGHT
                    "U" -> UP
                    "L" -> LEFT
                    "D" -> DOWN

                    else -> UNKNOWN
                }
            }
        }
    }

    val input = mutableListOf<String>()

    override fun common(input: Array<String>) {
        this.input.addAll(input)
    }

    override fun part01(): String {
        val tailPositions = mutableListOf<String>()
        val head = Point()
        val tail = Point()
        input.forEach {
            it.split(" ").let {
                val direction = Direction.getDirection(it[0])
                val steps = it[1].toInt()

                for (i in 0 until steps) {
                    // Move head
                    head.move(head.x + direction.xMod, head.y + direction.yMod)
                    if ((head.x - tail.x).absoluteValue > 1 || (head.y - tail.y).absoluteValue > 1) {
                        // Tail has to follow
                        if ((head.x - tail.x).absoluteValue > 1 && head.y == tail.y) {
                            // in x direction
                            tail.x += direction.xMod
                        } else if (head.x == tail.x && (head.y - tail.y).absoluteValue > 1) {
                            // in y direction
                            tail.y += direction.yMod
                        } else {
                            // diagonally
                            val (vec, length) = tail.getMovementVector(head)
                            vec.mask()
                            tail.move(tail.x + vec.x, tail.y + vec.y)
                        }
                    }
                    if (!tailPositions.contains("${tail.x};${tail.y}")) {
                        tailPositions.add("${tail.x};${tail.y}")
                    }
                }
                /*
                var y = 0
                for (_y in 0 until 5) {
                    y = 4 - _y
                    for (x in 0 until 6) {
                        if (head.x == x && head.y == y) {
                            print("H")
                        } else if (x == 0 && y == 0) {
                            print("s")
                        } else if (tail.x == x && tail.y == y) {
                            print("T")
                        } else {
                            print(".")
                        }
                    }
                    print("\n")
                }
                print("")
                 */
            }
        }
        return tailPositions.size.toString()
    }

    override fun forceTest(): Boolean {
        return false
    }

    override fun part02(): String {
        val tailPositions = mutableListOf<String>()
        val knots = Array(10) { Point() }
        input.forEach {
            it.split(" ").let {
                val direction = Direction.getDirection(it[0])
                val steps = it[1].toInt()

                for (i in 0 until steps) {
                    // Move head
                    knots[0].move(knots[0].x + direction.xMod, knots[0].y + direction.yMod)
                    for (knotPos in 1 until knots.size) {
                        val head = knots[knotPos - 1]
                        val tail = knots[knotPos]
                        if ((head.x - tail.x).absoluteValue > 1 || (head.y - tail.y).absoluteValue > 1) {
                            // Tail has to follow
                            if ((head.x - tail.x).absoluteValue > 1 && head.y == tail.y) {
                                // in x direction
                                tail.x += direction.xMod
                            } else if (head.x == tail.x && (head.y - tail.y).absoluteValue > 1) {
                                // in y direction
                                tail.y += direction.yMod
                            } else {
                                // diagonally
                                val (vec, length) = tail.getMovementVector(head)
                                vec.mask()
                                tail.move(tail.x + vec.x, tail.y + vec.y)
                            }
                        }
                        knots[knotPos - 1] = head
                        knots[knotPos] = tail
                    }
                    if (!tailPositions.contains("${knots[9].x};${knots[9].y}")) {
                        tailPositions.add("${knots[9].x};${knots[9].y}")
                    }
                }
            }
        }
        return tailPositions.size.toString()
    }

    fun Point.getMovementVector(goal: Point): Pair<Vec2D, Int> { // Vector, length
        val vec = Vec2D(goal.x - x, goal.y - y)
        return Pair(vec, vec.normalize())
    }

}
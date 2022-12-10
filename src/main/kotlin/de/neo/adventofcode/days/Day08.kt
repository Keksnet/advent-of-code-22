package de.neo.adventofcode.days

class Day08 : IDay {

    var xMax = 0
    var yMax = 0
    val grid = mutableMapOf<String, Int>()
    val seen = mutableListOf<String>()

    override fun common(input: Array<String>) {
        xMax = input[0].length
        yMax = input.size
        input.forEachIndexed { y, it ->
            it.forEachIndexed { x, high ->
                grid["$x;$y"] = high.code - 0x30
            }
        }
    }

    override fun part01(): String {
        var visibleTrees = 0
        for (y in 0 until yMax) {
            val highest = Array(2) { -1 }
            for (x in 0 until xMax) {
                val xReverse = (xMax - 1) - x
                val xHeight = grid["$x;$y"]!!
                val xReverseHeight = grid["$xReverse;$y"]!!
                x@ for (i in 0..1) {
                    val importantX = if (i == 0) x else xReverse
                    val importantHeight = if (i == 0) xHeight else xReverseHeight
                    if (importantHeight > highest[i]) {
                        highest[i] = importantHeight
                        if (seen.contains("$importantX;$y")) continue@x
                        seen.add("$importantX;$y")
                        visibleTrees++
                    }
                }

            }
        }
        for (x in 0 until xMax) {
            val highest = Array(2) { -1 }
            for (y in 0 until yMax) {
                val yReverse = (yMax - 1) - y
                val yHeight = grid["$x;$y"]!!
                val yReverseHeight = grid["$x;$yReverse"]!!
                y@ for (i in 0..1) {
                    val importantY = if (i == 0) y else yReverse
                    val importantHeight = if (i == 0) yHeight else yReverseHeight
                    if (importantHeight > highest[i]) {
                        if (importantHeight == 9 && x == 3) {
                            print("")
                        }
                        highest[i] = importantHeight
                        if (seen.contains("$x;$importantY")) continue@y
                        seen.add("$x;$importantY")
                        visibleTrees++
                    }
                }
            }
        }
        /*
        for (y in 0 until yMax) {
            for (x in 0 until xMax) {
                if (seen.contains("$x;$y")) print("\u001b[31m${grid["$x;$y"]}\u001b[0m")
                else print("${grid["$x;$y"]}")
            }
            print("\n\u001B[0m")
        }
         */
        return visibleTrees.toString()
    }

    override fun part02(): String {
        val viewDistances = mutableListOf<Int>()
        for (y in 1 until yMax - 1) {
            for (x in 1 until xMax - 1) {
                val houseHeight = grid["$x;$y"]!!
                val viewDistance = Array(4) { -1 }
                x1@ for (x1 in 1 until xMax) {
                    val xRight = x + x1
                    val xLeft = x - x1
                    if (viewDistance[0] == -1 && xRight >= xMax) viewDistance[0] = x1 - 1
                    if (viewDistance[1] == -1 && xLeft < 0) viewDistance[1] = x1 - 1
                    if (viewDistance[0] == -1 && grid["$xRight;$y"]!! >= houseHeight) viewDistance[0] = x1
                    if (viewDistance[1] == -1 && grid["$xLeft;$y"]!! >= houseHeight) viewDistance[1] = x1
                    if (viewDistance[0] != -1 && viewDistance[1] != -1) break@x1
                }
                y1@ for (y1 in 1 until yMax) {
                    val yDown = y + y1
                    val yUp = y - y1
                    if (viewDistance[2] == -1 && yDown >= yMax) viewDistance[2] = y1 - 1
                    if (viewDistance[3] == -1 && yUp < 0) viewDistance[3] = y1 - 1
                    if (viewDistance[2] == -1 && grid["$x;$yDown"]!! >= houseHeight) viewDistance[2] = y1
                    if (viewDistance[3] == -1 && grid["$x;$yUp"]!! >= houseHeight) viewDistance[3] = y1
                    if (viewDistance[2] != -1 && viewDistance[3] != -1) break@y1
                }
                viewDistances.add(viewDistance.reduce { acc, i -> acc * i })
            }
        }
        return viewDistances.max().toString()
    }

}
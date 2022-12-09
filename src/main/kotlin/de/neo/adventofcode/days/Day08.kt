package de.neo.adventofcode.days

import java.awt.Point

class Day08 : IDay {

    val grid = mutableMapOf<Int, MutableMap<Int, Int>>() // x = (y = high)

    override fun common(input: Array<String>) {
        val p = Point()
        input.forEach {
            it.forEach {
                val gridMap = (grid[p.x] ?: mutableMapOf())
                gridMap[p.y] = it.code - 0x30
                grid[p.x] = gridMap
                p.x++
            }
            p.x = 0
            p.y++
        }
        println(grid)
    }

    override fun part01(): String {
        var visibleTrees = 0
        val seen = mutableListOf<String>()
        grid.forEach { (x, yMap) ->
            var highest = 0
            for (entry in yMap) {
                if (entry.value < highest) {
                    break
                }
                if (seen.contains("$x;${entry.key}")) continue
                highest = entry.value
                visibleTrees++
                seen.add("$x;${entry.key}")
            }
            highest = 0
            for (entry in yMap.map { Pair(it.key, it.value) }.reversed()) {
                if (entry.second < highest) {
                    break
                }
                if (seen.contains("$x;${entry.first}")) continue
                highest = entry.second
                visibleTrees++
                seen.add("$x;${entry.first}")
            }
        }
        for (y in 0 until grid[0]!!.size) {
            var highest = 0
            for (x in 0 until grid.size) {
                if (grid[x]?.get(y)!! < highest) {
                    break
                }
                if (seen.contains("$x;${y}")) continue
                highest = grid[x]?.get(y)!!
                visibleTrees++
                seen.add("$x;${y}")
            }
        }
        for (y in 0 until grid[0]!!.size) {
            for (x in 0 until grid.size) {
                if (seen.contains("$x;$y")) {
                    print("\u001b[31m${grid[x]?.get(y)!!}\u001b[0m")
                    continue
                }
                print("${grid[x]?.get(y)!!}")
            }
            print("\n")
        }
        return visibleTrees.toString()
    }

    override fun part02(): String {
        return ""
    }

}
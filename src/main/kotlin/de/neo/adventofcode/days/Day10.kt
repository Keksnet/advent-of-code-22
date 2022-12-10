package de.neo.adventofcode.days

import java.awt.Point
import kotlin.math.absoluteValue

class Day10 : IDay {

    private val xValues = mutableListOf<Int>()

    override fun common(input: Array<String>) {
        val addRegex = Regex("addx (-?\\d+)")
        var cycle = 0
        var x = 1
        input.forEach {
            if (it == "noop") {
                cycle++
                xValues.add(x)
            } else if (it.matches(addRegex)) {
                val arg0 = addRegex.matchEntire(it)!!.destructured.component1().toInt()
                for (i in 0..1) {
                    cycle++
                    xValues.add(x)
                }
                x += arg0
            }
        }
    }

    override fun part01(): String {
        return xValues.mapIndexed { index, i -> i * (index + 1) }.filterIndexed { index, _ -> ((index + 1) - 20) % 40 == 0 }.sum().toString()
    }

    override fun part02(): String {
        val pos = Point()
        val builder = StringBuilder()
        for (y in 0 until 6) {
            pos.y = y
            for (x in 0 until 40) {
                pos.x = x
                if ((xValues[pos.y * 40 + pos.x] - pos.x).absoluteValue <= 1) {
                    builder.append("#")
                } else {
                    builder.append(".")
                }
            }
            builder.append("\n")
        }
        return "\n${builder.toString().trim()}"
    }
}
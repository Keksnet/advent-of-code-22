package de.neo.adventofcode.days

class Day01 : IDay {

    private val calories = mutableListOf<Int>()

    override fun common(input: Array<String>) {
        var elve = 0
        input.forEach {
            if (it.isBlank()) elve++
            else {
                if (calories.size == elve) calories.add(0)
                calories[elve] += it.toInt()
            }
        }
        calories.sortDescending()
    }

    override fun part01(): String {
        return calories[0].toString()
    }

    override fun part02(): String {
        return (calories[0] + calories[1] + calories[2]).toString()
    }
}
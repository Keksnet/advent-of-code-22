package de.neo.adventofcode.days

class Day02 : IDay {

    val input = mutableListOf<String>()
    val solutions = mutableMapOf<String, Int>()

    init {
        solutions["A X"] = 3 + 1
        solutions["A Y"] = 6 + 2
        solutions["A Z"] = 0 + 3
        solutions["B X"] = 0 + 1
        solutions["B Y"] = 3 + 2
        solutions["B Z"] = 6 + 3
        solutions["C X"] = 6 + 1
        solutions["C Y"] = 0 + 2
        solutions["C Z"] = 3 + 3
    }

    override fun common(input: Array<String>) {
        this.input.addAll(input)
    }

    override fun part01(): String {
        val rounds = mutableListOf<Int>()
        input.forEach {
            try {
                rounds.add(solutions[it]!!)
            }catch (_: Exception) {
                println(it)
            }
        }
        return rounds.sum().toString()
    }

    override fun part02(): String {
        val newInput = mutableListOf<String>()
        input.forEach {
            val strategy = it.split(" ").toTypedArray()
            if (strategy.size == 2) {
                val newStrategy = if (strategy[1] == "Z") {
                    if (strategy[0] == "A") "Y" else if (strategy[0] == "B") "Z" else "X"
                } else if (strategy[1] == "X") {
                    if (strategy[0] == "A") "Z" else if (strategy[0] == "B") "X" else "Y"
                } else {
                    if (strategy[0] == "A") "X" else if (strategy[0] == "B") "Y" else "Z"
                }
                newInput.add("${strategy[0]} $newStrategy")
            } else {
                println("Invalid row: $it")
            }
        }
        input.clear()
        input.addAll(newInput)
        return part01()
    }
}
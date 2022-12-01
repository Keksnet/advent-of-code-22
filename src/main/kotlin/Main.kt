import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
    val input = Files.readAllLines(Path.of("day01.txt"))

    val calories = mutableListOf<Int>()
    var elve = 0
    input.forEach {
        if (it.isBlank()) {
            elve++
        }else {
            if (calories.size == elve) calories.add(0)
            calories[elve] += it.toInt()
        }
    }

    // Part 1
    println(calories.max())

    // Part 2
    calories.sortDescending()

    println(calories[0] + calories[1] + calories[2])
}
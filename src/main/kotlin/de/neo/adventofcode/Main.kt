package de.neo.adventofcode

import de.neo.adventofcode.days.IDay
import de.neo.adventofcode.days.Day00
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.util.Calendar

fun main() {
    val day = run {
        var dayWeek = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
        while (dayWeek.length < 2) {
            dayWeek = "0$dayWeek"
        }
        dayWeek
    }

    var dayFile = Path.of("input")
    if (!Files.exists(dayFile)) {
        Files.createDirectory(dayFile)
    }

    dayFile = dayFile.resolve("day$day")
    if (!Files.exists(dayFile)) {
        Files.createDirectory(dayFile)
    }

    createIfNotExists(dayFile.resolve("input.txt"))
    createIfNotExists(dayFile.resolve("sample.txt"))
    for (i in 1..2) {
        createIfNotExists(dayFile.resolve("solution_$i.txt"))
    }

    val sample = Files.readAllLines(dayFile.resolve("sample.txt"))
    if (sample.size == 0) {
        println("Sample is empty. STOP.")
        return
    }
    for (i in 1..2) {
        val dayInstance = retrieveDayInstance(day)
        val solution = Files.readString(dayFile.resolve("solution_$i.txt")).trim()
        if (solution.isBlank()) {
            println("Solution for Part $i is empty. Skipping...")
            continue
        }
        dayInstance.common(sample.toTypedArray())
        val result = if (i == 1) dayInstance.part01() else dayInstance.part02()
        if (result == solution) {
            println("Test for Part $i SUCCESS.")
            continue
        }
        if (dayInstance.forceTest()) {
            println("Test for Part $i FAILED. Stop.")
            return
        }
        println("Expected '$solution' got '$result' instead.")
        println("Test for Part $i FAILED.")
    }

    val dayInstance = retrieveDayInstance(day)
    val input = Files.readAllLines(dayFile.resolve("input.txt"))
    if (input.size == 0) {
        println("Input is empty. STOP.")
        return
    }

    dayInstance.common(input.toTypedArray())
    println("Solution (Part 1): ${dayInstance.part01()}")
    println("Solution (Part 2): ${dayInstance.part02()}")
    println("Done.")
}

fun retrieveDayInstance(day: String): IDay {
    return try {
        val dayClass = Class.forName("de.neo.adventofcode.days.Day$day")
        dayClass.getConstructor().newInstance() as IDay
    } catch (_: Exception) {
        Day00()
    }
}

fun createIfNotExists(path: Path) {
    if (!Files.exists(path)) {
        Files.createFile(path)
    }
}
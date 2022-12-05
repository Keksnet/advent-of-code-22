package de.neo.adventofcode.days

import de.neo.adventofcode.createIfNotExists
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.regex.Pattern

class Day05 : IDay {

    val instructionRegex = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)")
    val stacks = mutableListOf<Deque<String>>()
    val moves = mutableListOf<String>()

    override fun common(input: Array<String>) {
        var crates = true
        var stackCounter = 0
        input.forEach {
            if (crates) {
                if (it.isBlank()) {
                    crates = false
                    for (i in stacks.indices) {
                        val oldStack = stacks[i].toList()
                        stacks[i].clear()
                        oldStack.forEach {
                            stacks[i].push(it)
                        }
                    }
                    return@forEach
                }
                if (it.contains('1')) {
                    return@forEach
                }

                val betterLine = run {
                    var pos = 0
                    var replaced = it.map { pos++; if (pos % 4 == 0 && pos != 0) ":" else it }.joinToString("")
                        .replace("   ", "[ ]")
                        .replace(":", " ")
                        .replace(Regex(" ?\\["), ",")
                        .replace(Regex("\\] ?"), "")
                    while(replaced.contains("  ")) {
                        replaced = replaced.replace("  ", " ")
                    }
                    replaced = replaced.subSequence(1, replaced.length).toString()
                    replaced
                }
                betterLine.split(",").forEach {
                    if (it.isBlank()) {
                        stackCounter++
                        return@forEach
                    }
                    while (stacks.size <= stackCounter) {
                        stacks.add(ArrayDeque())
                    }
                    stacks[stackCounter].push(it)
                    stackCounter++
                }
                stackCounter = 0
            } else {
                moves.add(it)
            }
        }
    }

    override fun part01(): String {
        val newStacks = arrayListOf<Deque<String>>()
        stacks.forEach {
            val t = ArrayDeque<String>()
            it.reversed().forEach { t.push(it) }
            newStacks.add(t)
        }
        println(newStacks)
        println(stacks)
        moves.forEach {
            val matcher = instructionRegex.matcher(it)
            while (matcher.find()) {
                val numMoved = matcher.group(1).toInt()
                val from = matcher.group(2).toInt() - 1
                val to = matcher.group(3).toInt() - 1

                for (i in 0 until numMoved) {
                    stacks[to].push(stacks[from].pop())
                }
            }
        }
        val filtered = stacks.filter { it.isNotEmpty() }
        stacks.clear()
        println(newStacks)
        println(stacks)
        newStacks.forEach {
            stacks.add(it)
        }
        println(newStacks)
        println(stacks)
        return filtered.map { it.pop() }.joinToString("")
    }

    override fun part02(): String {
        moves.forEach {
            val matcher = instructionRegex.matcher(it)
            while (matcher.find()) {
                val numMoved = matcher.group(1).toInt()
                val from = matcher.group(2).toInt() - 1
                val to = matcher.group(3).toInt() - 1

                println("m $numMoved f $from t $to")

                val temp = Array(numMoved) { "" }

                for (i in 0 until numMoved) {
                    temp[(numMoved - 1) - i] = stacks[from].pop()
                }
                temp.forEach {
                    stacks[to].push(it)
                }
            }
        }
        return stacks.filter { it.isNotEmpty() }.map { it.pop() }.joinToString("")
    }

}
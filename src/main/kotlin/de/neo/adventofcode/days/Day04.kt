package de.neo.adventofcode.days

class Day04 : IDay {

    val sections = mutableListOf<Pair<IntRange, IntRange>>()

    override fun common(input: Array<String>) {
        input.forEach {
            val pair = it.split(",")
            val sections0 = run {
                val sectionStr = pair[0].split("-")
                IntRange(sectionStr[0].toInt(), sectionStr[1].toInt())
            }
            val sections1 = run {
                val sectionStr = pair[1].split("-")
                IntRange(sectionStr[0].toInt(), sectionStr[1].toInt())
            }

            sections.add(Pair(sections0, sections1))
        }
    }

    override fun part01(): String {
        var sum = 0
        sections.forEach {
            if (it.first.contains(it.second) || it.second.contains(it.first)) {
                sum++
            }
        }
        return sum.toString()
    }

    override fun part02(): String {
        val found = mutableListOf<Pair<IntRange, IntRange>>()
        sections.forEach {
            if (it.first.any { it0 -> it.second.contains(it0) }) {
                found.add(it)
            }
        }
        return found.size.toString()
    }

    fun IntRange.contains(range: IntRange): Boolean {
        return this.first <= range.first && this.last >= range.last
    }

}
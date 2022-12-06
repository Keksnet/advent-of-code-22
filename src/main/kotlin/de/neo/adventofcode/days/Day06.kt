package de.neo.adventofcode.days

class Day06 : IDay {

    var input = ""

    override fun common(input: Array<String>) {
        this.input = input[0]
    }

    override fun part01(): String {
        return findPacketMarker(4).toString()
    }

    override fun part02(): String {
        return findPacketMarker(14).toString()
    }

    fun findPacketMarker(size: Int): Int {
        return input.windowed(size).indexOfFirst { packetMarkerChars ->
            var ret = false
            packetMarkerChars.forEach {
                if (ret) return@forEach
                if (packetMarkerChars.occurrences(it) > 1) ret = true
            }
            return@indexOfFirst !ret
        } + size
    }

    fun String.occurrences(c: Char): Int {
        var occurences = 0
        for (char in this) {
            if (char == c) occurences++
        }
        return occurences
    }

}
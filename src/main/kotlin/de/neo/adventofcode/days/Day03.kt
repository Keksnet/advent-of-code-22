package de.neo.adventofcode.days

class Day03 : IDay {

    val input = mutableListOf<String>()

    override fun common(input: Array<String>) {
        this.input.addAll(input)
    }

    fun convertToInt(char: Char): Int {
        var code = char.code
        if (char.isLowerCase()) {
            code -= 0x61
            code += 1
        } else if (char.isUpperCase()) {
            code -= 0x41
            code += 27
        } else {
            println("INVALID CHAR: $char")
        }
        return code

    }

    override fun part01(): String {
        val errors = mutableListOf<Char>()
        val backpack = mutableListOf<Pair<String, String>>()
        input.forEach {
            val compartmentLen = it.length / 2
            backpack.add(Pair(it.subSequence(0, compartmentLen).toString(), it.subSequence(compartmentLen, it.length).toString()))
        }
        val localErrors = mutableListOf<Char>()
        backpack.forEach { bp ->
            bp.first.forEach { item ->
                if (bp.second.contains(item, ignoreCase = false) && !localErrors.contains(item)) {
                    localErrors.add(item)
                }
            }
            errors.addAll(localErrors)
            localErrors.clear()
        }
        return errors.sumOf { convertToInt(it) }.toString()
    }

    override fun part02(): String {
        val badges = mutableListOf<Char>()
        val localBadges = mutableListOf<Char>()
        input.windowed(3, 3, partialWindows = false).forEach { groupBackpacks ->
            groupBackpacks[0].forEach { item ->
                if (groupBackpacks[1].contains(item) && groupBackpacks[2].contains(item) && !localBadges.contains(item)) {
                    localBadges.add(item)
                }
            }
            badges.addAll(localBadges)
            localBadges.clear()
        }
        return badges.sumOf { convertToInt(it) }.toString()
    }

}
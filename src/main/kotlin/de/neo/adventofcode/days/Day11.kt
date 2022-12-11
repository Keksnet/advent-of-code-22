package de.neo.adventofcode.days

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.properties.Delegates

class Day11 : IDay {

    inner class Operation(operatorChar: Char, valueString: String) {
        var operator by Delegates.notNull<Char>()
        var value by Delegates.notNull<BigDecimal>()

        init {
            if (valueString == "old") {
                if (operatorChar == '+') {
                    operator = '*'
                    value = BigDecimal.valueOf(2)
                } else if (operatorChar == '*') {
                    operator = '^'
                    value = BigDecimal.ZERO
                }
            } else {
                operator = operatorChar
                value = BigDecimal.valueOf(valueString.toLong())
            }
        }

        fun perform(x: BigDecimal): BigDecimal {
            val usedValue = value
            return when (operator) {
                '+' -> x + usedValue
                '*' -> x * usedValue
                '^' -> x * x
                else -> x
            }
        }
    }

    inner class Monkey(val id: Int, val operation: Operation, val testDivider: BigDecimal, val targets: IntArray) {
        val items = mutableListOf<BigDecimal>()
        var inspectionCounter: BigDecimal = BigDecimal.valueOf(0)
        val bigThree: BigDecimal = BigDecimal.valueOf(3)

        fun performRound() {
            items.map { operation.perform(it) }
                .map { it.divide(bigThree, 0, RoundingMode.DOWN) }
                .forEach {
                    inspectionCounter++
                    monkeys[targets[if (it % testDivider == BigDecimal.ZERO) 0 else 1]]!!.items.add(it)
                }
            items.clear()
        }

        fun performRoundStress() {
            items.map {
                val op = operation.perform(it)
                return@map op
            }.forEach {
                inspectionCounter++
                monkeys[targets[if (it % testDivider == BigDecimal.ZERO) 0 else 1]]!!.items.add(it)
            }
            items.clear()
        }
    }

    private val monkeys = mutableMapOf<Int, Monkey>()
    private val input = mutableListOf<String>()

    override fun common(input: Array<String>) {
        this.input.clear()
        this.input.addAll(input)
        // Fine regex: Monkey (?<monkeyid>\d):\n {2}Starting items: (?<items>(?>\d+(?>, )?)+)\n {2}Operation: new = old (?<operator>[+*]) (?<operationvalue>\d+|old)\n {2}Test: divisible by (?<testnumber>\d+)\n {4}If true: throw to monkey (?<targettrue>\d+)\n {4}If false: throw to monkey (?<targetfalse>\d+)
        // Optimized: [\w ]+(?<monkeyid>\d):\n[\w ]+: (?<items>(?>\d+(?>, )?)+)\n[\w ]+:[\w =]+(?<operator>[+*]) (?<operationvalue>\d+|old)\n[\w ]+:[\w ]+ (?<testnumber>\d+)\n[\w ]+:[\w ]+(?<targettrue>\d+)\n[\w ]+:[\w ]+(?<targetfalse>\d+)
        val monkeyRegex =
            Regex("[\\w ]+(?<monkeyId>\\d):\\n[\\w ]+: (?<items>(?>\\d+(?>, )?)+)\\n[\\w ]+:[\\w =]+(?<operator>[+*]) (?<operationValue>\\d+|old)\\n[\\w ]+:[\\w ]+ (?<testDivider>\\d+)\\n[\\w ]+:[\\w ]+(?<targetTrue>\\d+)\\n[\\w ]+:[\\w ]+(?<targetFalse>\\d+)")
        input.joinToString("\n").split("\n\n").forEach {
            if (it.matches(monkeyRegex)) {
                val regMonk = monkeyRegex.matchEntire(it)
                val groups = regMonk?.groups!!
                val monkeyId = groups["monkeyId"]!!.value.toInt()
                val monkey = Monkey(
                    monkeyId,
                    Operation(groups["operator"]!!.value[0], groups["operationValue"]!!.value),
                    BigDecimal.valueOf(groups["testDivider"]!!.value.toLong()),
                    intArrayOf(groups["targetTrue"]!!.value.toInt(), groups["targetFalse"]!!.value.toInt())
                )
                groups["items"]!!.value.split(", ").forEach {
                    monkey.items.add(BigDecimal.valueOf(it.toLong()))
                }
                monkeys[monkeyId] = monkey
            } else {
                println("Invalid input: $it")
            }
        }
    }

    fun simulateCycles(cycles: Int) {
        val monkeysVal = monkeys.map { it.value }
        for (i in 0 until cycles) {
            monkeysVal.forEach { it.performRound() }
            if (i % 100 == 0) {
                println("$i/$cycles")
            }
        }
    }

    fun simulateCyclesStress(cycles: Int) {
        val monkeysVal = monkeys.map { it.value }
        for (i in 0 until cycles) {
            monkeysVal.forEach { it.performRoundStress() }
            if (i % 100 == 0) {
                println("$i/$cycles")
            }
        }
    }

    override fun part01(): String {
        simulateCycles(20)
        val monkeyScore = monkeys.map { it.value }.sortedByDescending { it.inspectionCounter }
        return (monkeyScore[0].inspectionCounter * monkeyScore[1].inspectionCounter).toString()
    }

    override fun part02(): String {
        monkeys.clear()
        common(input.toTypedArray())

        val monkeyInfo = List<Array<Number>>(monkeys.size) { // business, opcode, opval, divider, tar_true, tar_fal
            val monkey = monkeys[it]!!
            arrayOf(BigDecimal.ZERO,
                monkey.operation.operator.code,
                monkey.operation.value,
                monkey.testDivider,
                monkey.targets[0],
                monkey.targets[1])
        }
        val monkeyItems = List(monkeys.size) { monkeys[it]!!.items }

        var mInfo: Array<Number>
        for (i in 0 until 10000) {
            for (mIndex in monkeyInfo.indices) {
                mInfo = monkeyInfo[mIndex]
                for (mIIndex in monkeyItems[mIndex].indices) {
                    monkeyInfo[mIndex][0] = (mInfo[0] as BigDecimal) + BigDecimal.ONE
                    val worried = when (mInfo[1]) {
                        '+'.code -> monkeyItems[mIndex][mIIndex] + (mInfo[2] as BigDecimal)
                        '*'.code -> monkeyItems[mIndex][mIIndex] * (mInfo[2] as BigDecimal)
                        '^'.code -> monkeyItems[mIndex][mIIndex] * monkeyItems[mIndex][mIIndex]
                        else -> BigDecimal.ZERO
                    }
                    monkeyItems[(if (worried % (mInfo[3] as BigDecimal) == BigDecimal.ZERO) mInfo[4] else mInfo[5]) as Int].add(worried)
                }
                monkeyItems[mIndex].clear()
            }
        }

        val monkeyScore = monkeys.map { it.value }.sortedByDescending { it.inspectionCounter }
        return (monkeyScore[0].inspectionCounter * monkeyScore[1].inspectionCounter).toString()
    }

}
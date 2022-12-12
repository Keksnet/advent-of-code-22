package de.neo.adventofcode.days

import kotlin.math.absoluteValue
import kotlin.math.pow

class Day12 : IDay {

    data class Pos2D(
        var x: Long = 0,
        var z: Long = 0
    ) {
        constructor(source: Pos2D) : this() {
            copyFrom(source)
        }

        fun copyFrom(source: Pos2D) {
            x = source.x
            z = source.z
        }

        fun distance(target: Pos2D): Long {
            return ((target.x - x).toDouble().pow(2) + (target.z - z).toDouble().pow(2)).toLong()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Pos2D) return false
            return x == other.x && z == other.z
        }

        override fun toString(): String {
            return "$x;$z"
        }
    }

    data class Node(
        val pos: Pos2D,
        val height: Long
    )

    data class PossiblePath(
        var cost: Long = 0L,
        var nodes: ArrayList<Node> = ArrayList()
    )

    private val startPos: Pos2D = Pos2D()
    private val endPos: Pos2D = Pos2D()
    private val nodes = mutableMapOf<String, Node>()
    private val paths = mutableListOf<PossiblePath>()

    override fun common(input: Array<String>) {
        val pos = Pos2D()
        input.forEach { row ->
            row.forEach { step ->
                val height = when (step) {
                    'S' -> {
                        startPos.copyFrom(pos)
                        0L
                    }

                    'E' -> {
                        endPos.copyFrom(pos)
                        25L
                    }

                    else -> {
                        (step.code - 0x61).toLong()
                    }
                }
                nodes[pos.toString()] = Node(Pos2D(pos), height)
                pos.x += 1
            }
            pos.z += 1
        }
    }

    private fun getUsableNeighbours(node: Node): List<Node> {
        return nodes.map { it.value }
            .filter { (it.pos.x - node.pos.x).absoluteValue <= 1 }
            .filter { (it.pos.z - node.pos.z).absoluteValue <= 1 }
            .filter { it.pos.x == it.pos.x || it.pos.z == node.pos.z }
            .filter { it.height - 1 <= node.height }
            .toList()
    }

    private fun findPath(currentNode: Node, path: PossiblePath) {
        getUsableNeighbours(currentNode).sortedBy { currentNode.pos.distance(it.pos) }.forEach {
            path.cost++
            path.nodes.add(it)
            if (it.pos == endPos) {
                return@forEach
            }
            findPath(it, path)
        }
    }

    private fun findPaths(currentNode: Node): List<PossiblePath> {
        val paths = mutableListOf<PossiblePath>()
        getUsableNeighbours(currentNode).sortedBy { currentNode.pos.distance(it.pos) }.forEach {
            val path = PossiblePath()
            paths.add(path)
            findPath(it, path)
        }
        return paths
    }

    override fun part01(): String {
        val startNode = nodes[startPos.toString()]!!
        println(findPaths(startNode).sortedBy { it.cost }.first())
        return ""
    }

    override fun part02(): String {
        return ""
    }

}
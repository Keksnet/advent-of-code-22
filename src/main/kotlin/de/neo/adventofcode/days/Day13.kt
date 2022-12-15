package de.neo.adventofcode.days

class Day13 : IDay {

    inner class PacketType(val list: MutableList<PacketType>?) {
        val isList: Boolean
            get() = list != null

        var int: Int = 0

        constructor(int: Int) : this(null) {
            this.int = int
        }

        fun compare(other: PacketType): Int {
            return if (other.isList == isList) {
                if (list!!.size - other.list!!.size != 0) {
                    return list.size - other.list.size
                }
                var comparison = 0
                list.forEachIndexed { index, packetType ->
                    if (comparison != 0) return@forEachIndexed
                    comparison = packetType.compare(other.list[index])
                    if (comparison != 0) {
                        return@forEachIndexed
                    }
                }
                comparison
            } else {
                int - other.int
            }
        }
    }

    private val packets = mutableListOf<Pair<PacketType, PacketType>>()

    private fun filterNonDigit(str: String): String {
        val regex = Regex("(\\D)")
        if (regex.containsMatchIn(str)) {
            return regex.replace(str, "")
        }
        return str
    }

    private fun parsePacketList(packetList: String): PacketType {
        val packetRegex = Regex("\\[(.+)\\]")
        val packetType = PacketType(mutableListOf())
        packetRegex.matchEntire(packetList)!!
            .groups[1]!!
            .value
            .split(",")
            .forEach {
                packetType.list!!.add(if (packetRegex.matches(it)) parsePacketList(it) else PacketType(filterNonDigit(it).toInt()))
            }
        return packetType
    }

    override fun common(input: Array<String>) {
        input.joinToString("\n").split("\n\n").forEach {
            it.split("\n").let {
                packets.add(Pair(parsePacketList(it[0]), parsePacketList(it[1])))
            }
        }
    }

    override fun part01(): String {
        println(packets[0])
        return ""
    }

    override fun part02(): String {
        return ""
    }

}
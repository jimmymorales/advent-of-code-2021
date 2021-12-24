fun main() {
    fun part1(input: List<String>): Int {
        return input.first()
            .mapNotNull { hexMap[it] }
            .joinToString(separator = "")
            .parsePacket()
            .totalVersion
    }

    fun part2(input: List<String>): Long {
        return input.first()
            .mapNotNull { hexMap[it] }
            .joinToString(separator = "")
            .parsePacket()
            .value
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 20)
    check(part2(testInput) == 1L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun String.parsePacket(initialIndex: Int = 0): Packet {
    var packetIndex = initialIndex
    val version = substring(packetIndex, packetIndex + 3).toInt(radix = 2)
    packetIndex += 3
    val type = substring(packetIndex, packetIndex + 3).toInt(radix = 2)
    packetIndex += 3

    // If literal type
    if (type == 4) {
        val literalBinary = substring(packetIndex)
            .windowedSequence(size = 5, step = 5)
            .takeWhile { it[0] == '1' }
            .joinToString(separator = "") { it.substring(startIndex = 1) }
            .let {
                val start = packetIndex + ((it.length / 4) * 5) + 1
                it + substring(start, start + 4)
            }
        return Packet.Literal(
            version,
            value = literalBinary.toLong(radix = 2),
            size = packetIndex + ((literalBinary.length / 4) * 5) - initialIndex
        )
    }
    val lengthTypeId = get(packetIndex)
    packetIndex += 1
    val packets = buildList {
        if (lengthTypeId == '0') {
            val length = substring(packetIndex, packetIndex + 15).toInt(radix = 2)
            packetIndex += 15
            val finalIndex = packetIndex + length
            while (packetIndex < finalIndex) {
                val newPacket = parsePacket(packetIndex)
                add(newPacket)
                packetIndex += newPacket.size
            }
        } else {
            val packetsCount = substring(packetIndex, packetIndex + 11).toInt(radix = 2)
            packetIndex += 11
            repeat(packetsCount) {
                val newPacket = parsePacket(packetIndex)
                add(newPacket)
                packetIndex += newPacket.size
            }
        }
    }
    val opType = type.toOpType() ?: error("Invalid op type")
    return Packet.OpSubPacket(version, packets, opType, size = packetIndex - initialIndex)
}

private enum class PacketType { Sum, Product, Minimum, Maximum, GreaterThan, LessThan, EqualTo }

private fun Int.toOpType() = when (this) {
    0 -> PacketType.Sum
    1 -> PacketType.Product
    2 -> PacketType.Minimum
    3 -> PacketType.Maximum
    5 -> PacketType.GreaterThan
    6 -> PacketType.LessThan
    7 -> PacketType.EqualTo
    else -> null
}

private sealed class Packet {
    abstract val value: Long
    abstract val version: Int
    abstract val size: Int

    data class Literal(override val version: Int, override val value: Long, override val size: Int) : Packet()

    data class OpSubPacket(
        override val version: Int,
        val packets: List<Packet>,
        val type: PacketType,
        override val size: Int,
    ) : Packet() {
        override val value: Long
            get() = when (type) {
                PacketType.Sum -> packets.sumOf(Packet::value)
                PacketType.Product -> packets.fold(1L) { acc, packet -> acc * packet.value }
                PacketType.Minimum -> packets.minOf(Packet::value)
                PacketType.Maximum -> packets.maxOf(Packet::value)
                PacketType.GreaterThan -> if (packets[0].value > packets[1].value) 1 else 0
                PacketType.LessThan -> if (packets[0].value < packets[1].value) 1 else 0
                PacketType.EqualTo -> if (packets[0].value == packets[1].value) 1 else 0
            }
    }
}

private val Packet.totalVersion: Int
    get() = version + when (this) {
        is Packet.Literal -> 0
        is Packet.OpSubPacket -> packets.sumOf(Packet::totalVersion)
    }

private val hexMap = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111",
)
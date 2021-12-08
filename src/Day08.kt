fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        line.split(" | ")[1].split(" ").count { it.length in listOf(2, 3, 4, 7) }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val entry = line.split(" | ")
            val signals = entry[0].split(" ").sortedBy(String::length).map { Signal(it.toList()) }.toMutableList()
            val segment = mutableMapOf<Char, Signal>()
            segment['1'] = signals[0].also { signals.removeFirst() }
            segment['7'] = signals[0].also { signals.removeFirst() }
            segment['4'] = signals[0].also { signals.removeFirst() }
            segment['8'] = signals.last().also { signals.removeLast() }
            segment['3'] = when {
                (segment['1']!! union signals[0]).key == signals[0].key -> signals[0]
                (segment['1']!! union signals[1]).key == signals[1].key -> signals[1]
                else -> signals[2]
            }.also { signals.remove(it) }
            segment['2'] = if ((segment['4']!! union signals[0]).key == segment['8']!!.key) {
                signals[0]
            } else {
                signals[1]
            }.also { signals.remove(it) }
            segment['5'] = signals[0].also { signals.removeFirst() }
            segment['9'] = (segment['5']!! union segment['7']!!).also { _9 -> signals.removeIf { _9.key == it.key } }
            segment['6'] = if ((segment['7']!! union signals[0]).key == segment['8']!!.key) {
                signals[0]
            } else {
                signals[1]
            }.also { signals.remove(it) }
            segment['0'] = signals[0]
            println(segment)
            entry[1].split(" ").map { digit ->
                val digitSignal = Signal(digit.toList())
                segment.entries.find { it.value.key == digitSignal.key }!!.key
            }.joinToString(separator = "").toLong().also { println(it) }
        }
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229L)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private data class Signal(val chars: List<Char>) {
    val key = chars.sorted()
}

private infix fun Signal.union(other: Signal): Signal =
    Signal(chars.toMutableSet().apply { addAll(other.chars) }.toList())
/**
 * a = 1
 * b = 2
 * c = 3
 * d = 4
 * e = 5
 * f = 6
 * g = 7
 */

/**
 * 1 = cf      = 9
 * 4 = bcdf    = 15
 * 7 = acf     = 10
 * 8 = abcdefg = 29
 *
 * 2 = acdeg   = 20
 * 3 = acdfg   = 21
 * 5 = abdfg   = 20
 * 6 = abdefg  = 25
 * 9 = abcdfg  = 23
 * 0 = abcefg  = 24
 */

/**
 * 3 => 1 + x5 = x5
 * 2 => 4 + x5 = 8
 * 5 // left
 * 6 => 7 + x6 = 8
 * 9 => 5 + 7
 * g = 1 + 5
 *
 */
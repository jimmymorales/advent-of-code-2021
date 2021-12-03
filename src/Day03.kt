import kotlin.math.pow
import kotlin.math.roundToInt

fun main() {
    fun part1(input: List<String>): Int {
        val diagLength = input.first().count()
        val gammaRate = input
            .fold(Array(diagLength) { 0 }) { acc, line ->
                acc.indices.forEach { index -> acc[index] += line[index].digitToInt() }
                acc
            }
            .joinToString(separator = "") { if (it > input.size / 2) "1" else "0" }
            .toInt(2)

        val epsilonRate = gammaRate xor (2.0.pow(diagLength).toInt() - 1)
        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        val oxygenGeneratorRating = input.calculateRating(bitIndex = 0, filterMostCommon = true)
        val co2ScrubberRating = input.calculateRating(bitIndex = 0, filterMostCommon = false)
        return oxygenGeneratorRating * co2ScrubberRating
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun List<String>.calculateRating(bitIndex: Int, filterMostCommon: Boolean): Int {
    val bit = map { it[bitIndex] }.count { it == '1' }
        .let { if (it >= (size / 2.0).roundToInt()) 1 else 0 }
        .let { if (filterMostCommon) it else it xor 0b1 }
        .digitToChar()
    val filtered = filter { it[bitIndex] == bit }
    return filtered.singleOrNull()?.toInt(2) ?: filtered.calculateRating(bitIndex + 1, filterMostCommon)
}

import kotlin.math.abs
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val crabs = input.first().split(",").map(String::toInt).sorted()
        return (crabs.first()..crabs.last())
            .map { n -> crabs.sumOf { abs(it - n) } }
            .minOf { it }
    }

    fun part2(input: List<String>): Int {
        val crabs = input.first().split(",").map { it.toInt() }.sorted()
        return (crabs.first()..crabs.last())
            .map { d -> crabs.sumOf { abs(it - d).toDouble().let { n -> (n.pow(2) + n) / 2 }.toInt() } }
            .minOf { it }
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

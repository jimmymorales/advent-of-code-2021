import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>, shouldFilterDiagonalVectors: Boolean = true): Int {
        return input.asSequence()
            .map { it.split(" -> ", ",").map(String::toInt).toVectorPoints(shouldFilterDiagonalVectors) }
            .flatten()
            .groupingBy { it }
            .fold(initialValue = 0) { acc, _ -> acc + 1 }
            .count { entry -> entry.value > 1 }
    }

    fun part2(input: List<String>): Int = part1(input, shouldFilterDiagonalVectors = false)

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

fun List<Int>.toVectorPoints(shouldFilterDiagonalVector: Boolean = true): List<String> {
    val (x1, y1, x2, y2) = this
    if (shouldFilterDiagonalVector && x1 != x2 && y1 != y2) return emptyList()
    return buildList {
        val xAcc = calculateDelta(x1, x2)
        val yAcc = calculateDelta(y1, y2)
        var x = x1
        var y = y1
        while (x in min(x1, x2)..max(x1, x2) && y in min(y1, y2)..max(y1, y2)) {
            add("$x,$y")
            x += xAcc
            y += yAcc
        }
    }
}

private fun calculateDelta(n1: Int, n2: Int) = when {
    n1 == n2 -> 0
    n1 > n2 -> -1
    else -> 1
}

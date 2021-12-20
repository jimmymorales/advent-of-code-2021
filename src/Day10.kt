fun main() {
    fun part1(input: List<String>): Int = input.sumOf { it.calculateError(ErrorType.Corrupted) }.toInt()

    fun part2(input: List<String>): Long =
        input.mapNotNull { line -> line.calculateError(ErrorType.Incomplete).takeIf { it > 0 } }
            .sorted()
            .middle

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private enum class ErrorType { Corrupted, Incomplete }

private fun String.calculateError(type: ErrorType): Long {
    val q = ArrayDeque<Char>(length)
    for (c in this) {
        when (c) {
            in tags -> q.add(c)
            tags[q.last()] -> q.removeLast()
            else -> return if (type == ErrorType.Corrupted) corruptedPoints.getValue(c) else 0
        }
    }
    return if (type == ErrorType.Incomplete) {
        q.asReversed().fold(0L) { total, c -> total * 5 + incompletePoints.getValue(c) }
    } else {
        0
    }
}

private val List<Long>.middle: Long get() = get(size / 2)

private val tags = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
private val corruptedPoints = mapOf(')' to 3L, ']' to 57L, '}' to 1197L, '>' to 25137L).withDefault { 0L }
private val incompletePoints = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4).withDefault { 0 }

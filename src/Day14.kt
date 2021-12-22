fun main() {
    fun part1(input: List<String>, steps: Int = 10): Long {
        val template = input.first().toMutableList()
        val insertionRules = input.drop(2).associate { it.take(2) to it.last() }
        val elementCount = template.groupingBy { it }.fold(initialValue = 0L) { count, _ -> count + 1 }.toMutableMap()
        val pairs = template.windowed(2) { it.joinToString(separator = "") }
            .groupingBy { it }
            .fold(0L) { count, _ -> count + 1 }
            .toMutableMap()
        repeat(steps) {
            pairs.filterValues { it > 0 }.forEach { (pair, count) ->
                val insert = insertionRules[pair] ?: return@forEach
                pairs.merge("${pair[0]}$insert", count) { old, new -> old + new }
                pairs.merge("$insert${pair[1]}", count) { old, new -> old + new }
                pairs.merge(pair, count * -1) { old, new -> old + new }

                elementCount.merge(insert, count) { old, new -> old + new }
            }
        }
        val maxElement = elementCount.maxOf { it.value }
        val minElement = elementCount.minOf { it.value }
        return maxElement - minElement
    }

    fun part2(input: List<String>): Long {
        return part1(input, steps = 40)
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

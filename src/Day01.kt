fun main() {
    fun part1(input: List<String>): Int = input.countByWindow(windowSize = 2) { (a, b) -> b > a }

    // a + b + c <=> b + c + d -> a <=> d
    fun part2(input: List<String>): Int = input.countByWindow(windowSize = 4) { (a, _, _, d) -> d > a }

    // test if implementation meets criteria from the description, like what?:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

fun List<String>.countByWindow(windowSize: Int, predicate: (List<Int>) -> Boolean) = map(String::toInt)
    .windowed(windowSize)
    .count(predicate)

fun main() {
    fun part1(input: List<String>): Int = input.countMeasurementIncrease(windowSize = 1)

    fun part2(input: List<String>): Int = input.countMeasurementIncrease(windowSize = 3)

    // test if implementation meets criteria from the description, like what?:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

fun List<String>.countMeasurementIncrease(windowSize: Int) = windowed(windowSize) { it.sumOf(String::toInt) }
    .zipWithNext()
    .count { it.second > it.first }

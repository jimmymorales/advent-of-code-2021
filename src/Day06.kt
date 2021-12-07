fun main() {
    fun part1(input: List<String>, days: Int = 80): Long {
        var timerMap = input.first()
            .split(",")
            .groupingBy(String::toInt)
            .fold(initialValue = 0L) { count, _ -> count + 1 }
        repeat(days) {
            val newTimerMap = mutableMapOf<Int, Long>().withDefault { 0 }
            timerMap.forEach { (i, c) ->
                if (i == 0) {
                    newTimerMap[6] = newTimerMap.getValue(6) + c
                    newTimerMap[8] = newTimerMap.getValue(8) + c
                } else {
                    newTimerMap[i - 1] = newTimerMap.getOrElse(i - 1) { 0 } + c
                }
            }
            timerMap = newTimerMap
        }
        return timerMap.values.sum()
    }

    fun part2(input: List<String>): Long = part1(input, days = 256)

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

fun main() {
    fun part1(input: List<String>): Int {
        val energyLevels = input.map { line -> line.map(Char::digitToInt).toMutableList() }.toMutableList()
        return (1..100).sumOf { energyLevels.energyGain().count() }
    }

    fun part2(input: List<String>): Int {
        val energyLevels = input.map { line -> line.map(Char::digitToInt).toMutableList() }.toMutableList()
        var step = 1
        while (step > 0) {
            val flashes = energyLevels.energyGain()
            if (flashes.count() == energyLevels.size * energyLevels[0].size) {
                return step
            }
            step++
        }
        return 0
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

private val diffX = listOf(-1, -1, -1, 0, 1, 1, 1, 0)
private val diffY = listOf(1, 0, -1, -1, -1, 0, 1, 1)

private fun MutableList<MutableList<Int>>.energyGain(): List<Coord> {
    val flashesCoords = mutableListOf<Coord>()
    for (y in indices) {
        for (x in get(y).indices) {
            val newLevel = this[y][x] + 1
            this[y][x] = if (newLevel != 10) newLevel else 0.also { flashesCoords.add(Coord(x, y)) }
        }
    }
    var coordIndex = 0
    while (coordIndex < flashesCoords.size) {
        val (x, y) = flashesCoords[coordIndex]
        for (di in 0..7) {
            val newY = y + diffY[di]
            val newX = x + diffX[di]
            if (newY !in indices
                || newX !in get(newY).indices
                || Coord(newX, newY) in flashesCoords
            ) {
                continue
            }
            val newLevel = this[newY][newX] + 1
            this[newY][newX] = if (newLevel != 10) newLevel else 0.also { flashesCoords.add(Coord(newX, newY)) }
        }
        coordIndex++
    }
    return flashesCoords.toList()
}

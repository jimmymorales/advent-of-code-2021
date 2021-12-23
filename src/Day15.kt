import java.util.PriorityQueue

fun main() {
    fun part1(input: List<String>, destination: Coord = Coord(input[0].lastIndex, input.lastIndex)): Int {
        val cave = input.map { row -> row.map(Char::digitToInt) }

        val visited = mutableSetOf<Coord>()
        val queue = PriorityQueue<Chiton>()
        queue.add(Chiton(Coord(x = 0, y = 0), riskLevel = 0))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.coord == destination) {
                return current.riskLevel
            }

            if (current.coord in visited) {
                continue
            }

            visited.add(current.coord)
            current.coord.neighbors(limitX = 0..destination.x, limitY = 0..destination.y)
                .forEach { coord ->
                    queue.offer(Chiton(coord, current.riskLevel + cave.get(coord)))
                }
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        return part1(input, destination = Coord((input[0].length * 5) - 1, (input.size * 5) - 1))
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}

private fun List<List<Int>>.get(coord: Coord): Int {
    val dx = coord.x / first().size
    val dy = coord.y / size
    val originalRisk = this[coord.y % this.size][coord.x % first().size]
    val newRisk = (originalRisk + dx + dy)
    return newRisk.takeIf { it < 10 } ?: (newRisk - 9)
}

private data class Chiton(val coord: Coord, val riskLevel: Int) : Comparable<Chiton> {
    override fun compareTo(other: Chiton) = riskLevel - other.riskLevel
}

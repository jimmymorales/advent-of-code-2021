import kotlin.math.absoluteValue

fun main() {
    /**
     * NOTE: I ended up following this blog post https://todd.ginsberg.com/post/advent-of-code/2021/day17/
     */

    fun part1(input: List<String>): Int {
        val targetRange = input.first().toTargetRange()
        return (0..targetRange.x.last).maxOf { x ->
            (targetRange.y.first..targetRange.y.first.absoluteValue).maxOf { y ->
                val track = probePositions((Coord(x, y))).takeWhile { !targetRange.hasOvershot(it) }
                if (track.any { it in targetRange }) track.maxOf { it.y } else 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val targetRange = input.first().toTargetRange()
        return (0..targetRange.x.last).sumOf { x ->
            (targetRange.y.first..targetRange.y.first.absoluteValue).count { y ->
                probePositions(Coord(x, y)).first { targetRange.hasOvershot(it) || it in targetRange } in targetRange
            }
        }
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}

private data class TargetRange(val x: IntRange, val y: IntRange)

private operator fun TargetRange.contains(coord: Coord): Boolean = coord.x in x && coord.y in y
private fun TargetRange.hasOvershot(coord: Coord): Boolean = coord.x > x.last || coord.y < y.first

private fun String.toTargetRange(): TargetRange =
    """.*x=(-?\d+)..(-?\d+).*y=(-?\d+)..(-?\d+)""".toRegex()
        .find(this)
        ?.destructured
        ?.let { (x1, x2, y1, y2) -> TargetRange(x = x1.toInt()..x2.toInt(), y = y1.toInt()..y2.toInt()) }
        ?: error("Wrong input!")

private fun probePositions(velocity: Coord): Sequence<Coord> = sequence {
    var position = Coord(0, 0)
    var actualVelocity = velocity
    while (true) {
        position = Coord(
            position.x + actualVelocity.x,
            position.y + actualVelocity.y
        )
        actualVelocity = Coord(
            actualVelocity.x + if (actualVelocity.x > 0) -1 else if (actualVelocity.x < 0) 1 else 0,
            actualVelocity.y - 1
        )
        yield(position)
    }
}

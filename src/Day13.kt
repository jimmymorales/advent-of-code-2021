fun main() {
    fun part1(input: List<String>): Int {
        val splitInputIndex = input.indexOf("")
        val points = input.take(splitInputIndex).map(String::toCoord).toSet()
        val folds = input.takeLast(input.lastIndex - splitInputIndex)
            .map { line -> line.substringAfterLast(" ").split("=").let { (axis, n) -> Fold(n.toInt(), axis.toAxis()) } }
        var firstFoldResult = 0
        val result = folds.foldIndexed(points) { index, folded, fold ->
            folded.fold(fold).also { if (index == 0) firstFoldResult = it.count() }
        }
        result.print()
        return firstFoldResult
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    //check(part2(testInput) == 36)

    val input = readInput("Day13")
    println(part1(input))
}

private enum class Axis { X, Y }
private data class Fold(val n: Int, val axis: Axis)

private fun String.toAxis() = if (this == "x") Axis.X else Axis.Y

private fun Set<Coord>.fold(fold: Fold): Set<Coord> {
    return map { coord ->
        when (fold.axis) {
            Axis.X -> if (coord.x < fold.n) coord else coord.copy(x = (2 * fold.n) - coord.x)
            Axis.Y -> if (coord.y < fold.n) coord else coord.copy(y = (2 * fold.n) - coord.y)
        }
    }.toSet()
}

private fun Set<Coord>.print() {
    val width = maxOf { it.x }
    val height = maxOf { it.y }
    for (y in 0..height) {
        for (x in 0..width) {
            print(if (Coord(x, y) in this) "#" else ".")
        }
        print("\n")
    }
}

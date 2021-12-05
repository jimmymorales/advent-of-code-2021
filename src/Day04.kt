fun main() {
    fun part1(input: List<String>, findLastWinner: Boolean = false): Int {
        val numbers = input[0].split(',').map(String::toInt)
        var boards = input.asSequence()
            .drop(2)
            .filter(String::isNotEmpty)
            .windowed(size = 5, step = 5)
            .map(List<String>::toBoard)
            .toList()
        var result = 0
        for (n in numbers) {
            if (boards.isEmpty()) break
            boards = boards.filter { board ->
                val isDone = board.markNumber(n)
                if (isDone) {
                    result = board.calculateUnmarkedSum() * n
                    if (!findLastWinner) return result
                }
                !isDone
            }
        }

        return result
    }

    fun part2(input: List<String>): Int = part1(input, findLastWinner = true)

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class Board(val columns: List<BoardLine>, val rows: List<BoardLine>) {
    fun markNumber(n: Int): Boolean {
        for (row in rows) {
            if (row.markItem(n) == 0) {
                return true
            }
        }
        for (column in columns) {
            if (column.markItem(n) == 0) {
                return true
            }
        }
        return false
    }

    fun calculateUnmarkedSum() = rows.sumOf { it.unmarkedItems.sum() }
}

data class BoardLine(val unmarkedItems: MutableList<Int>) {
    fun markItem(n: Int): Int {
        if (unmarkedItems.binarySearch(n) != -1) {
            unmarkedItems -= n
        }
        return unmarkedItems.size
    }
}

fun List<String>.toBoard(): Board {
    val rows = map(String::toBoardLine)
    val columns = rows.indices.map { x ->
        BoardLine(unmarkedItems = indices.map { y -> rows[y].unmarkedItems[x] }.sorted().toMutableList())
    }
    rows.forEach { it.unmarkedItems.sort() }
    return Board(columns, rows)
}

fun String.toBoardLine() = BoardLine(
    unmarkedItems = windowed(size = 3, step = 3, partialWindows = true) { it.trim().toString().toInt() }.toMutableList()
)
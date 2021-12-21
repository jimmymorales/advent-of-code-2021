fun main() {
    fun part1(input: List<String>, allowOneSmallCaveVisitTwice: Boolean = false): Int {
        val adjacencies = input.map { line -> line.split("-").let { it[0] to it[1] } }
            .let { r -> r + r.map { it.second to it.first } }
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })

        return visitVertex("start", adjacencies, allowOneSmallCaveVisitTwice)
    }

    fun part2(input: List<String>): Int {
        return part1(input, allowOneSmallCaveVisitTwice = true)
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private fun visitVertex(
    vertex: String,
    adjacencies: Map<String, List<String>>,
    allowOneSmallCaveVisitTwice: Boolean = false,
    visitedVertex: MutableList<String> = mutableListOf()
): Int {
    if (vertex.isSmallCave) {
        when {
            (vertex == "start" || !allowOneSmallCaveVisitTwice) && vertex in visitedVertex -> return 0
            allowOneSmallCaveVisitTwice -> {
                val hasAnyCaveBeenVisitedTwice = visitedVertex.asSequence()
                    .filter { it.isSmallCave }
                    .groupingBy { it }
                    .eachCount()
                    .any { (_, count) -> count == 2 }
                if (vertex in visitedVertex && hasAnyCaveBeenVisitedTwice) return 0
            }
        }
    }

    if (vertex == "end") {
        return 1
    }

    visitedVertex.add(vertex)

    return adjacencies.getValue(vertex)
        .sumOf { visitVertex(it, adjacencies, allowOneSmallCaveVisitTwice, visitedVertex) }
        .also { visitedVertex.removeLast() }
}

private val String.isSmallCave: Boolean get() = this == lowercase()

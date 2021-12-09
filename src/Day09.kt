fun main() {
    fun part1(input: List<String>): Int {
        val heightMap = input.map { it.toList().map(Char::digitToInt) }
        val lowpoints = buildList {
            for (iy in heightMap.indices) {
                for ((ix, height) in heightMap[iy].withIndex()) {
                    val top = heightMap.getOrNull(iy - 1)?.getOrNull(ix) ?: Int.MAX_VALUE
                    val right = heightMap.getOrNull(iy)?.getOrNull(ix + 1) ?: Int.MAX_VALUE
                    val bottom = heightMap.getOrNull(iy + 1)?.getOrNull(ix) ?: Int.MAX_VALUE
                    val left = heightMap.getOrNull(iy)?.getOrNull(ix - 1) ?: Int.MAX_VALUE
                    if (height < top && height < right && height < bottom && height < left) {
                        add(height)
                    }
                }
            }
        }
        return lowpoints.sum() + lowpoints.size
    }

    fun part2(input: List<String>): Int {
        val heightMap = input.map { it.toList().map(Char::digitToInt) }
        val dy = listOf(-1, 0, 1, 0)
        val dx = listOf(0, 1, 0, -1)
        val basinSizes = buildList {
            val seen = mutableSetOf<Pair<Int, Int>>()
            for (iy in heightMap.indices) {
                for ((ix, height) in heightMap[iy].withIndex()) {
                    val yx = iy to ix
                    if (yx !in seen && height != 9) {
                        var size = 0
                        val q = ArrayDeque<Pair<Int, Int>>()
                        q.add(yx)
                        while (q.isNotEmpty()) {
                            val (y, x) = q.removeFirst()
                            if ((y to x) in seen) continue
                            seen.add(y to x)
                            size++
                            dy.indices.forEach { d ->
                                val ny = y + dy[d]
                                val nx = x + dx[d]
                                val n = heightMap.getOrNull(ny)?.getOrNull(nx)
                                if (n != null && n != 9) {
                                    q.add(ny to nx)
                                }
                            }
                        }
                        add(size)
                    }
                }
            }
        }
        return basinSizes.sorted().takeLast(3).fold(1) { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

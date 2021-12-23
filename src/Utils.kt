import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

data class Coord(val x: Int, val y: Int)

fun Coord.neighbors(
    includeDiagonal: Boolean = false,
    limitX: IntRange = 0..Int.MAX_VALUE,
    limitY: IntRange = 0..Int.MAX_VALUE
): List<Coord> = buildList {
    add(Coord(x + 1, y))
    add(Coord(x + -1, y))
    add(Coord(x, y + 1))
    add(Coord(x, y - 1))
    if (includeDiagonal) {
        add(Coord(x + 1, y + 1))
        add(Coord(x + 1, y - 1))
        add(Coord(x - 1, y + 1))
        add(Coord(x - 1, y - 1))
    }
}.filter { it.x in limitX && it.y in limitY }

/**
 * Converts x,y String to Pair
 */
fun String.toCoord() = split(",").let { (x, y) -> Coord(x.toInt(), y.toInt()) }
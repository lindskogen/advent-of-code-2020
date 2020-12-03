package day03

import java.io.File
import kotlin.time.measureTimedValue


fun traverseMap(dx: Int, dy: Int, map: List<CharArray>): Long {
    var x = 0
    var y = 0
    var numTrees = 0L

    while (y < map.size) {
        if (map[y][x % map[y].size] == '#') {
            numTrees += 1
        }
        x += dx
        y += dy
    }
    return numTrees
}



@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val map = File("src/main/kotlin/day03/input").readLines().map { it.toCharArray() }
        solve(map)
    }
    println(elapsed)
}

private fun solve(map: List<CharArray>) {
    val x3 = traverseMap(3, 1, map)
    println("Part 1: $x3")

    val x1 = traverseMap(1, 1, map)
    val x5 = traverseMap(5, 1, map)
    val x7 = traverseMap(7, 1, map)
    val y2 = traverseMap(1, 2, map)

    println("Part 2: ${x1 * x3 * x5 * x7 * y2}")
}

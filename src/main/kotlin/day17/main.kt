package day17

import java.io.File
import kotlin.time.measureTimedValue

data class Point(val x: Int, val y: Int, val z: Int, val w: Int)

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day17/input").readLines()

        println("Part 1: ${solve(lines, false)}")
        println("Part 2: ${solve(lines, true)}")
    }
    println(elapsed)
}


fun solve(lines: List<String>, simulateW: Boolean): Int {
    val map = mutableMapOf<Point, Boolean>()

    lines.forEachIndexed { x, line ->
        line.forEachIndexed { y, c ->
            map[Point(x, y, 0, 0)] = c == '#'
        }
    }

    var minZ = 0
    var maxZ = 0
    var minW = 0
    var maxW = 0
    var minX = map.keys.minByOrNull { p -> p.x }!!.x
    var maxX = map.keys.maxByOrNull { p -> p.x }!!.x
    var minY = map.keys.minByOrNull { p -> p.y }!!.y
    var maxY = map.keys.maxByOrNull { p -> p.y }!!.y


    for (i in 1..6) {
        // printCube(map, minZ..maxZ, minX..maxX, minY..maxY)


        val step = map.toMutableMap()
        minX--
        minY--
        minZ--
        maxX++
        maxY++
        maxZ++

        if (simulateW) {
            minW--
            maxW++
        }

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    for (w in minW..maxW) {
                        val p = Point(x, y, z, w)
                        val neighbors = countNeighbors(step, p)
                        map[p] = getNextState(step.getOrDefault(p, false), neighbors)
                    }
                }
            }
        }

    }

    return map.count { entry -> entry.value }

}

private fun printCube(
    map: MutableMap<Point, Boolean>,
    zs: IntRange,
    xs: IntRange,
    ys: IntRange,
) {
    for (z in zs) {
        println("z = $z")
        for (x in xs) {
            for (y in ys) {
                print(if (map[Point(x, y, z, 0)] == true) '#' else '.')
            }
            println()
        }
        println()
        println()
    }
}

private fun getNextState(
    prevState: Boolean,
    neighbors: Int
) = if (prevState) {
    neighbors == 2 || neighbors == 3
} else {
    neighbors == 3
}

private fun countNeighbors(
    step: Map<Point, Boolean>,
    p: Point
): Int {
    var neighbors = 0

    for (x in -1..1) {
        for (y in -1..1) {
            for (z in -1..1) {
                for (w in -1..1) {
                    if (!(x == 0 && y == 0 && z == 0 && w == 0)) {
                        if (step.getOrDefault(Point(p.x + x, p.y + y, p.z + z, p.w + w), false)) {
                            neighbors++
                        }
                    }
                }
            }

        }
    }
    return neighbors
}



package day11

import java.io.File
import kotlin.time.measureTimedValue


@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day11/input").readLines().map { it.toCharArray() }

        val p1 = solve(lines.map { it.clone() }, 4, 1)
        println("Part 1: $p1")
        val p2 = solve(lines, 5, Int.MAX_VALUE)
        println("Part 2: $p2")
    }
    println(elapsed)
}

fun solve(lines: List<CharArray>, numTolerate: Int, maxVision: Int): Int {
    var changed: Boolean

    do {
        changed = false
        val oldLines = lines.map { it.clone() }

        for (line in lines.indices) {
            for (seat in lines[line].indices) {
                when (oldLines[line][seat]) {
                    'L' -> {
                        if (countOccupied(oldLines, line, seat, maxVision) == 0) {
                            lines[line][seat] = '#';
                            changed = true
                        }
                    }
                    '#' -> {
                        if (countOccupied(oldLines, line, seat, maxVision) >= numTolerate) {
                            lines[line][seat] = 'L';
                            changed = true
                        }
                    }
                }
            }
        }
    } while (changed)

    return lines.sumOf { it.count { c -> c == '#' } }
}


private fun countOccupied(
    lines: List<CharArray>,
    line: Int,
    seat: Int,
    maxVision: Int
): Int {
    var count = 0

    for (dl in -1..1) {
        for (ds in -1..1) {
            if (dl == 0 && ds == 0) {
                continue
            }

            for (step in 1..maxVision) {
                val l = line + (dl * step)
                val s = seat + (ds * step)

                if (l !in lines.indices || s !in lines[0].indices) {
                    break
                }

                when (lines[l][s]) {
                    'L' -> {
                        break
                    }
                    '#' -> {
                        count++
                        break
                    }
                }
            }
        }
    }

    return count
}

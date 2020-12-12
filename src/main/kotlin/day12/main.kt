package day12

import java.io.File
import kotlin.math.abs
import kotlin.time.measureTimedValue


enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun turnLeftDegrees(deg: Int): Direction {
        var d = this
        for(s in 1..(deg / 90)) {
            d = d.turnLeft()
        }
        return d
    }

    fun turnRightDegrees(deg: Int): Direction {
        var d = this
        for(s in 1..(deg / 90)) {
            d = d.turnRight()
        }
        return d
    }

    fun turnLeft(): Direction {
        return when (this) {
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
            NORTH -> WEST
        }
    }

    fun turnRight(): Direction {
        return when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
    }
}


@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day12/input").readLines()
        val p1 = solve1(lines)
        println("Part 1: $p1")

        val p2 = solve2(lines)
        println("Part 2: $p2")
    }
    println(elapsed)
}

private fun solve1(lines: List<String>): Int {
    var (x, y) = Pair(0, 0)
    var dir = Direction.EAST


    for (l in lines) {
        val arg = l.substring(1).toInt()

        when (l[0]) {
            'R' -> {
                dir = dir.turnRightDegrees(arg)
            }
            'L' -> {
                dir = dir.turnLeftDegrees(arg)
            }
            'F' -> {
                when (dir) {
                    Direction.NORTH -> {
                        y += arg
                    }
                    Direction.EAST -> {
                        x += arg
                    }
                    Direction.SOUTH -> {
                        y -= arg
                    }
                    Direction.WEST -> {
                        x -= arg
                    }
                }
            }
            'N' -> {
                y += arg
            }
            'S' -> {
                y -= arg
            }
            'E' -> {
                x += arg
            }
            'W' -> {
                x -= arg
            }
        }
    }

    return abs(x) + abs(y)
}

private fun solve2(lines: List<String>): Int {
    var (shipX, shipY) = Pair(0, 0)
    var (wpX, wpY) = Pair(10, 1)

    for (l in lines) {
        val arg = l.substring(1).toInt()

        when (l[0]) {
            'R' -> {
                for(s in 1..(arg / 90)) {
                    val (tx, ty) = Pair(wpX, wpY)
                    wpX = ty
                    wpY = -tx
                }
            }
            'L' -> {
                for(s in 1..(arg / 90)) {
                    val (tx, ty) = Pair(wpX, wpY)
                    wpX = -ty
                    wpY = tx
                }
            }
            'F' -> {
               shipX += wpX * arg
               shipY += wpY * arg
            }
            'N' -> {
                wpY += arg
            }
            'S' -> {
                wpY -= arg
            }
            'E' -> {
                wpX += arg
            }
            'W' -> {
                wpX -= arg
            }
        }

    }

    return abs(shipX) + abs(shipY)
}

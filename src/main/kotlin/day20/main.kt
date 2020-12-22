package day20

import day04.chomp
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.sqrt
import kotlin.time.measureTimedValue


// Solution from https://todd.ginsberg.com/post/advent-of-code/2020/day20/

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val squares = File("src/main/kotlin/day20/input").readText().chomp().split("\n\n");

        solve(squares)
    }
    println(elapsed)
}

enum class Orientation {
    North,
    East,
    South,
    West;
}

data class Tile(val id: Long, var body: Array<CharArray>) {

    private val sides: Set<String> = Orientation.values().map { sideFacing(it) }.toSet()
    private val sidesReversed = sides.map { it.reversed() }.toSet()

    private fun hasSide(side: String): Boolean =
        side in sides || side in sidesReversed

    private fun flip(): Tile {
        body = body.map { it.reversed().toCharArray() }.toTypedArray()
        return this
    }

    private fun rotateClockwise(): Tile {
        body = body.mapIndexed { x, row ->
            row.mapIndexed { y, _ ->
                body[y][x]
            }.reversed().toCharArray()
        }.toTypedArray()

        return this
    }

    fun orientations(): Sequence<Tile> = sequence {
        repeat(2) {
            repeat(4) {
                yield(this@Tile.rotateClockwise())
            }
            this@Tile.flip()
        }
    }

    fun insetRow(row: Int): String =
        body[row].drop(1).dropLast(1).joinToString("")

    fun sharedSideCount(tiles: List<Tile>): Int =
        sides.sumOf { side ->
            tiles
                .filterNot { it.id == id }
                .count { tile -> tile.hasSide(side) }
        }

    fun isSideShared(dir: Orientation, tiles: List<Tile>): Boolean =
        tiles
            .filterNot { it.id == id }
            .any { tile -> tile.hasSide(sideFacing(dir)) }

    fun findAndOrientNeighbor(mySide: Orientation, theirSide: Orientation, tiles: List<Tile>): Tile {
        val mySideValue = sideFacing(mySide)

        return tiles
            .filterNot { it.id == id }
            .first { it.hasSide(mySideValue) }
            .also { it.orientToSide(mySideValue, theirSide) }
    }

    private fun orientToSide(side: String, direction: Orientation) =
        orientations().first { it.sideFacing(direction) == side }

    private fun sideFacing(dir: Orientation): String =
        when (dir) {
            Orientation.North -> body.first().joinToString("")
            Orientation.South -> body.last().joinToString("")
            Orientation.West -> body.map { row -> row.first() }.joinToString("")
            Orientation.East -> body.map { row -> row.last() }.joinToString("")
        }

    fun maskIfFound(mask: List<Point2D>): Boolean {
        var found = false
        val maxWidth = mask.maxOf { it.y }
        val maxHeight = mask.maxOf { it.x }

        (0..(body.size - maxHeight)).forEach { x ->
            (0..(body.size - maxWidth)).forEach { y ->
                val lookingAt = Point2D(x, y)
                val actualSpots = mask.map { it + lookingAt }
                if (actualSpots.all { body[it.x][it.y] == '#' }) {
                    found = true
                    actualSpots.forEach { body[it.x][it.y] = '0' }
                }

            }
        }
        return found
    }
}


fun solve(squares: List<String>) {
    val tiles = squares.map {
        val rows = it.split("\n")
        val id = rows[0].dropLast(1).substring(5).toLong()
        val body = rows.drop(1).map { it.toCharArray() }.toTypedArray()
        Tile(id, body)
    }.toMutableList()


    val image = createImage(tiles)

    val part1 = image.first().first().id *
            image.first().last().id *
            image.last().first().id *
            image.last().last().id

    println("Part 1: $part1")


    val seaMonsterOffsets = listOf(
        Point2D(0, 18), Point2D(1, 0), Point2D(1, 5), Point2D(1, 6), Point2D(1, 11), Point2D(1, 12),
        Point2D(1, 17), Point2D(1, 18), Point2D(1, 19), Point2D(2, 1), Point2D(2, 4), Point2D(2, 7),
        Point2D(2, 10), Point2D(2, 13), Point2D(2, 16)
    )

    val part2 = imageToSingleTile(tiles, image).orientations().first { it.maskIfFound(seaMonsterOffsets) }
        .body
        .sumBy { row -> row.count { char -> char == '#' } }


    println("Part 2: $part2")
}

private fun createImage(tiles: List<Tile>): List<List<Tile>> {
    val width = sqrt(tiles.count().toFloat()).toInt()
    var mostRecentTile: Tile = findTopCorner(tiles)
    var mostRecentRowHeader: Tile = mostRecentTile

    return (0 until width).map { row ->
        (0 until width).map { col ->
            when {
                row == 0 && col == 0 -> mostRecentTile
                col == 0 -> {
                    mostRecentRowHeader =
                        mostRecentRowHeader.findAndOrientNeighbor(Orientation.South, Orientation.North, tiles)
                    mostRecentTile = mostRecentRowHeader
                    mostRecentRowHeader
                }
                else -> {
                    mostRecentTile =
                        mostRecentTile.findAndOrientNeighbor(Orientation.East, Orientation.West, tiles)
                    mostRecentTile
                }
            }
        }
    }
}


private fun imageToSingleTile(tiles: List<Tile>, image: List<List<Tile>>): Tile {
    val rowsPerTile = tiles.first().body.size
    val body = image.flatMap { row ->
        (1 until (rowsPerTile - 1)).map { y ->
            row.joinToString("") { it.insetRow(y) }.toCharArray()
        }
    }.toTypedArray()

    return Tile(0, body)
}

private fun findTopCorner(tiles: List<Tile>): Tile {
    return tiles
        .first { tile -> tile.sharedSideCount(tiles) == 2 }
        .orientations()
        .first {
            it.isSideShared(Orientation.South, tiles) && it.isSideShared(Orientation.East, tiles)
        }
}


data class Point2D(val x: Int, val y: Int) {
    operator fun plus(other: Point2D): Point2D =
        Point2D(x + other.x, y + other.y)

    operator fun times(by: Int): Point2D =
        Point2D(x * by, y * by)

    infix fun distanceTo(other: Point2D): Int =
        (x - other.x).absoluteValue + (y - other.y).absoluteValue

    fun rotateLeft(): Point2D =
        Point2D(x = y * -1, y = x)

    fun rotateRight(): Point2D =
        Point2D(x = y, y = x * -1)

    companion object {
        val ORIGIN = Point2D(0, 0)
    }
}

package day05

import java.io.File
import kotlin.time.measureTimedValue

private fun binarySearch(string: String): Int {
    val binaryString = string
        .replace(Regex("[LF]"), "0")
        .replace(Regex("[RB]"), "1")

    return binaryString.toInt(2)
}

private fun findMissingSeat(res: List<Int>): Int {
    var last = res[0]

    for (r in res.drop(1)) {
        if (last + 1 != r) {
            return last + 1
        }
        last = r;
    }
    return -1
}

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val seats = File("src/main/kotlin/day05/input").readLines()
            .map { binarySearch(it) }

        solve(seats)
    }
    println(elapsed)
}

private fun solve(seats: List<Int>) {
    val sortedSeats = seats.sorted()
    println("Part 1: ${sortedSeats.last()}")
    println("Part 2: ${findMissingSeat(sortedSeats)}")
}



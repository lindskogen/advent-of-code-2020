package day05

import java.io.File
import kotlin.time.measureTimedValue

private fun binarySearch(string: String): Int {
    var rMin = 0
    var rMax = 127
    var rHalf = 128 / 2

    var cMin = 0
    var cMax = 7
    var cHalf = 8 / 2

    for (s in string) {
        when (s) {
            'L' -> {
                cMax -= cHalf
                cHalf /= 2
            }
            'R' -> {
                cMin += cHalf
                cHalf /= 2
            }
            'F' -> {
                rMax -= rHalf
                rHalf /= 2
            }
            'B' -> {
                rMin += rHalf
                rHalf /= 2
            }
        }
    }
    return rMin * 8 + cMin

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
        val seats = File("src/main/kotlin/day05/input").readLines().map { binarySearch(it)}.sorted()
        solve(seats)
    }
    println(elapsed)
}

private fun solve(seats: List<Int>) {
    println("Part 1: ${seats.maxOrNull()}")
    println("Part 2: ${findMissingSeat(seats)}")
}



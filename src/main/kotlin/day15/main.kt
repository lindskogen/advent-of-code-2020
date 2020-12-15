package day15

import day04.chomp
import java.io.File
import kotlin.collections.HashMap
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day15/input").readText().chomp() .split(',').map { it.toInt() }
        val p1 = solve(lines, 2020)
        println("Part 1: $p1")


        val p2 = solve(lines, 30000000)
        println("Part 2: $p2")
    }
    println(elapsed)
}

fun solve(nums: List<Int>, nth: Int): Int {
    val map = Array(nth) { intArrayOf(0, 0) }
    var lastNum = 0
    var turn = 1
    for (n in nums) {
        map[n][0] = turn
        lastNum = n
        turn++
    }
    while (turn <= nth) {
        val n = lastNum
        val a = map[n]
        lastNum = if (a[1] != 0) {
            a[0] - a[1]
        } else {
            0
        }

        val b = map[lastNum]
        b[1] = b[0];
        b[0] = turn

        turn++
    }
    return lastNum
}

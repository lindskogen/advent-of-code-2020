package day10

import java.io.File
import kotlin.time.measureTimedValue


@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day10/input").readLines().map { it.toInt() }.sorted()

        println("Part 1: ${solve(lines)}")
        println("Part 2: ${solvePart2(lines)}")
    }
    println(elapsed)
}

fun solve(lines: List<Int>): Int {
    var jolt = 0
    var ones = 0
    var threes = 0
    for (adapter in lines) {
        if (adapter - jolt == 3) {
            threes++
        } else  if (adapter - jolt == 1) {
            ones++
        }
        jolt = adapter
    }
    threes++
    return ones * threes
}

fun solvePart2(lines: List<Int>): Long {
    val set = lines.toSet()
    val memo = mutableMapOf<Int, Long>()

    val res = recurse(memo, set, 0, lines.last())

    println("memo: ${memo.size}")

    return res
}

fun recurse(memo: MutableMap<Int, Long>, set: Set<Int>, jolt: Int, last: Int): Long {
    val memoValue = memo[jolt]
    if (memoValue != null) {
        return memoValue
    }

    if (jolt == last) {
        return 1L
    }

    val r1 = if (jolt + 1 in set) {
        recurse(memo, set, jolt + 1, last)
    } else 0

    val r2 = if (jolt + 2 in set) {
        recurse(memo, set, jolt + 2, last)
    } else 0

    val r3 = if (jolt + 3 in set) {
        recurse(memo, set, jolt + 3, last)
    } else 0

    val res = r1 + r2 + r3

    memo[jolt] = res

    return res

}

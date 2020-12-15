package day15

import day04.chomp
import java.io.File
import java.util.*
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
    val map = mutableMapOf<Int, LinkedList<Int>>()
    var lastNum = 0
    var turn = 1
    for (n in nums) {
        val linkedList = LinkedList<Int>()
        linkedList.add(turn)
        map[n] = linkedList
        lastNum = n
        turn++
    }
    while (turn <= nth) {
        val n = lastNum
        val a = map[n]
        if (a != null && a.size > 1) {
            lastNum = a[0] - a[1]
        } else {
            lastNum = 0
        }

        val b = map.getOrPut(lastNum, { LinkedList<Int>() })
        b.addFirst(turn)
        if (b.size > 2) {
            b.removeLast()
        }

        turn++
    }
    return lastNum
}


fun solve2(lines: List<Int>): Long {
   return 1L
}


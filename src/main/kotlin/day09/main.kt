package day09

import java.io.File
import kotlin.time.measureTimedValue


@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val instructions = File("src/main/kotlin/day09/input").readLines().map { it.toLong() }

        val part1 = solve(instructions, 25)
        if (part1 != null) {
            println("Part 1: $part1")
            val part2 = solvePart2(instructions, part1)
            println("Part 2: $part2")
        }


    }
    println(elapsed)
}

fun solvePart2(numbers: List<Long>, part1: Long): Long? {
    loop@for (i in numbers.indices) {
        var sum = 0L
        val list = mutableListOf<Long>()
        for (j in (i+1)..numbers.size) {
            sum += numbers[j]
            list.add(numbers[j])
            if (sum == part1) {
                return (list.minOrNull() ?: 0) + (list.maxOrNull() ?: 0)
            } else if (sum > part1) {
                continue@loop
            }
        }
    }
    return null
}

fun solve(numbers: List<Long>, chunk: Int): Long? {
    loop@ for (i in numbers.indices.drop(chunk)) {
        for (j in (i-chunk)..i) {
            for (k in j..i) {
                if (numbers[j] + numbers[k] == numbers[i]) {
                    continue@loop
                }
            }
        }
        return numbers[i]
    }
    return null
}


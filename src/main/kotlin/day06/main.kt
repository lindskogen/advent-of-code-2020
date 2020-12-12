package day06

import day04.chomp
import java.io.File
import kotlin.time.measureTimedValue

fun countPart1(string: String): Int {
    val set = mutableSetOf<Char>()

    for (s in string) {
        if (s != '\n') {
            set.add(s)
        }
    }
    return set.count()
}

fun countPart2(string: String): Int {
    val people = string.split("\n").count()

    val map = string.groupingBy { it }.eachCount()

    return map.filterValues { it == people }.count()
}


@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val questions = File("src/main/kotlin/day06/input").readText().chomp()
            .split("\n\n")
        solve(questions)
    }
    println(elapsed)
}

private fun solve(passports: List<String>) {
    println("Part 1: ${passports.sumOf { countPart1(it) }}")
    println("Part 2: ${passports.sumOf { countPart2(it) }}")
}

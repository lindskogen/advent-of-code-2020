package day02

import java.io.File
import java.util.regex.Pattern
import kotlin.time.measureTimedValue

data class Password(val min: Int, val max: Int, val ch: Char, val string: String) {
    fun isValidPart1(): Boolean {
        val count = string.count { it == ch }
        return count in min..max
    }

    fun isValidPart2(): Boolean {
        val firstMatches = string[min - 1] == ch
        val secondMatches = string[max - 1] == ch
        return firstMatches != secondMatches
    }

    companion object {
        private val regex = Pattern.compile("(-| |: )")!!

        fun parse(string: String): Password {

            val parts = string.split(regex)

            return Password(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                parts[2][0],
                parts[3],
            )
        }
    }
}


@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val nums = File("src/main/kotlin/day02/input").readLines().map(Password.Companion::parse)
        solve(nums)
    }
    println(elapsed)
}

private fun solve(passwords: List<Password>) {
    println("Part 1: ${passwords.count(Password::isValidPart1)}")
    println("Part 2: ${passwords.count(Password::isValidPart2)}")
}

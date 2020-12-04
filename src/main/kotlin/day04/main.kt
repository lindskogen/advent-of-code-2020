package day04

import java.io.File
import java.util.regex.Pattern
import kotlin.time.measureTimedValue

fun String.chomp(): String {
    return this.removeSuffix("\n")
}

fun String.splitIntoPairs(delimiters: String): Pair<String, String> {
    val parts = this.split(delimiters, limit = 2)
    return parts[0] to parts[1]
}

val WHITESPACE_REGEX = Pattern.compile("\\s")!!

fun String.splitWhitespace(): List<String> {
    return this.split(WHITESPACE_REGEX)
}


data class PassportHeight(val height: Int, val unit: String) {

    fun isValid(): Boolean {
        return (unit == "cm" && height in 150..193) ||
                (unit == "in" && height in 59..76)
    }

    companion object {
        fun parse(string: String): PassportHeight? {
            if (string.length < 3) {
                return null
            }

            val unit = string.takeLast(2)
            val height = Integer.parseInt(string.dropLast(2))

            if (unit != "cm" && unit != "in") {
                return null
            }

            return PassportHeight(height, unit)
        }
    }
}


class Passport(string: String) {

    private val fields: Map<String, String>
    private val height: PassportHeight?

    init {
        val map = string.splitWhitespace()
            .map { it.splitIntoPairs(":") }.toMap()

        fields = map
        height = map["hgt"]?.let { PassportHeight.parse(it) }
    }

    fun isValidPart1(): Boolean {
        return mandatoryFields.all { fields.containsKey(it) }
    }

    fun isValidPart2(): Boolean {
        return height?.isValid() == true &&
            rangeFields.all { (key, range) ->
                fields[key] != null && Integer.parseInt(fields[key]) in range
            } &&
            patternFields.all { (key, regex) ->
                fields[key]?.matches(regex) == true
            }
    }

    companion object {
        val mandatoryFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

        val rangeFields = listOf(
            Pair("byr", 1920..2002),
            Pair("iyr", 2010..2020),
            Pair("eyr", 2020..2030)
        )

        val patternFields = listOf(
            Pair("hcl", Regex("#[0-9a-f]{6}")),
            Pair("ecl", Regex("(amb|blu|brn|gry|grn|hzl|oth)")),
            Pair("pid", Regex("[0-9]{9}"))
        )
    }
}

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val passports = File("src/main/kotlin/day04/input").readText().chomp()
            .split(Pattern.compile("\n\n"))
            .map { Passport(it) }
        solve(passports)
    }
    println(elapsed)
}

private fun solve(passports: List<Passport>) {
    println("Part 1: ${passports.count { it.isValidPart1() }}")
    println("Part 2: ${passports.count { it.isValidPart2() }}")
}



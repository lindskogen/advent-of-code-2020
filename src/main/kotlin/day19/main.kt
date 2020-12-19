package day19

import day04.chomp
import java.io.File
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day19/input").readText().chomp().split("\n\n")
        val rules = lines[0].split("\n")
        val words = lines[1].split("\n")

        println("Part 1: ${solve(rules, words, false)}")
        println("Part 2: ${solve(rules, words, true)}")
    }
    println(elapsed)
}

sealed class Rule {
    class Value(val char: Char): Rule()
    class Ors(val ors: List<List<Int>>): Rule()
}


fun solve(rules: List<String>, words: List<String>, isPart2: Boolean): Int {
    val rulesMap = rules.map { rule ->
        val parts = rule.split(": ", limit = 2)
        val id = parts[0].toInt()
        val data = parts[1]
        if (data.contains('"')) {
            val c = data[1]
            id to Rule.Value(c)
        } else {
            val ors = data.split(" | ").map { or -> or.split(' ').map { it.toInt() } }
            id to Rule.Ors(ors)
        }
    }.toMap().toMutableMap()

    if (isPart2) {
        rulesMap[8] = Rule.Ors(
            listOf(
                listOf(42),
                listOf(42, 8)
            )
        )

        rulesMap[11] = Rule.Ors(
            listOf(
                listOf(42, 31),
                listOf(42, 11, 31),
            )
        )
    }

    val ruleId = 0

    return words.count { word ->
        val diff = matchPart(rulesMap, ruleId, word).maxOrNull()
        diff == word.length
    }
}

private fun matchPart(
    rules: Map<Int, Rule>,
    ruleId: Int,
    word: String,
    i: Int = 0
): List<Int> {
    return when (val rule = rules[ruleId]) {
        is Rule.Value -> {
            if (i < word.length && rule.char == word[i]) {
                listOf(i + 1)
            } else emptyList()
        }
        is Rule.Ors -> {
            rule.ors.flatMap { r ->
                r.fold(listOf(i)) { offsets, ruleId ->
                    offsets.flatMap { matchPart(rules, ruleId, word, it) }
                }
            }
        }
        else -> {
            emptyList()
        }
    }
}



package day16

import day04.chomp
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day16/input").readText().chomp()
            .split("\n\n")

        solve(lines)
    }
    println(elapsed)
}

data class Rule(val name: String, val ranges: List<IntRange>)

fun solve(lines: List<String>) {
    val rules = lines[0].split('\n').map { line ->
        val parts = line.split(": ")
        val ruleName = parts[0]
        val ranges = parts[1].split(" or ").map { r ->
            val rs = r.split('-').map { it.toInt() }
            rs[0]..rs[1]
        }
        Rule(ruleName, ranges)
    }

    val myTicket = parseTicket(lines[1].split('\n')[1])

    val tickets = lines[2].split('\n').drop(1).map { parseTicket(it) }

    var sum = 0

    val validTickets = tickets.filter { ticket ->
        ticket.all { num ->
            val keep = (rules.any { r -> r.ranges.any { it.contains(num) } })

            if (!keep) {
                sum += num
            }

            keep
        }
    }

    println("Part 1: $sum")

    val ruleOrder = Array<Rule?>(rules.size) { null }
    val potentialRules = mutableListOf<MutableSet<Rule>>()

    repeat(rules.size) {
        potentialRules.add(rules.toMutableSet())
    }

    for (ticket in validTickets) {
        innerLoop@ for (index in ticket.indices) {
            if (ruleOrder[index] != null) {
                continue@innerLoop
            }

            val num = ticket[index]
            potentialRules[index].removeIf { rule ->
                !rule.ranges.any { it.contains(num) }
            }

            if (potentialRules[index].size == 1) {
                val foundRule = potentialRules[index].first()
                handleFoundRule(index, foundRule, ruleOrder, potentialRules)
            }
        }
    }

    var changed: Boolean

    do {
        changed = false

        for (index in potentialRules.indices) {
            if (potentialRules[index].size == 1) {
                val foundRule = potentialRules[index].first()
                changed = handleFoundRule(index, foundRule, ruleOrder, potentialRules) || changed
            }
        }

    } while (changed)

    assertTrue(ruleOrder.all { it != null })

    val yourProduct = ruleOrder.foldIndexed(1L) { index, acc, rule ->
        if (rule!!.name.startsWith("departure")) {
            acc * myTicket[index]
        } else {
            acc
        }
    }


    assertEquals(5977293343129, yourProduct)

    println("Part 2: $yourProduct")
}

private fun parseTicket(line: String) = line.split(',').map { it.toInt() }

private fun handleFoundRule(
    index: Int,
    foundRule: Rule,
    ruleOrder: Array<Rule?>,
    potentialRules: MutableList<MutableSet<Rule>>
): Boolean {
    ruleOrder[index] = foundRule
    var changed = false
    potentialRules.forEach {
        if (it.remove(foundRule)) {
            changed = true
        }
        if (it.size == 1) {
            changed = true
        }
    }
    return changed
}

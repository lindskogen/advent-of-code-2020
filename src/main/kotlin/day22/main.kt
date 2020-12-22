package day22

import day04.chomp
import java.io.File
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day22/input").readText().chomp()
            .split("\n\n")
            .map { player ->
                player.split("\n").drop(1).map { it.toInt() }
            }
        println("Part 1: ${solvePart1(lines)}")
        println("Part 2: ${solvePart2(lines).second}")
    }
    println(elapsed)
}

fun solvePart1(startingDecks: List<List<Int>>): Int {
    val decks = startingDecks.map { it.toMutableList() }

    while (!decks.any { it.isEmpty() }) {
        val player1Card = decks[0].removeFirst()
        val player2Card = decks[1].removeFirst()

        if (player1Card > player2Card) {
            decks[0].add(player1Card)
            decks[0].add(player2Card)
        } else {
            decks[1].add(player2Card)
            decks[1].add(player1Card)
        }
    }

    return if (decks[0].size > 0) {
        calculateScore(decks[0])
    } else {
        calculateScore(decks[1])
    }
}

fun solvePart2(startingDecks: List<List<Int>>): Pair<Int, Int> {
    val decks = startingDecks.map { it.toMutableList() }
    val previousDecks = mutableSetOf<Pair<List<Int>, List<Int>>>()

    while (!decks.any { it.isEmpty() }) {

        if (!previousDecks.add(Pair(decks[0], decks[1]))) {
            return Pair(0, calculateScore(decks[0]))
        }

        val player1Card = decks[0].removeFirst()
        val player2Card = decks[1].removeFirst()

        if (decks[0].size >= player1Card && decks[1].size >= player2Card) {
            val innerGameWinner = solvePart2(
                listOf(
                    decks[0].subList(0, player1Card),
                    decks[1].subList(0, player2Card)
                )
            ).first
            if (innerGameWinner == 0) {
                decks[0].add(player1Card)
                decks[0].add(player2Card)
            } else {
                decks[1].add(player2Card)
                decks[1].add(player1Card)
            }
        } else {
            if (player1Card > player2Card) {
                decks[0].add(player1Card)
                decks[0].add(player2Card)
            } else {
                decks[1].add(player2Card)
                decks[1].add(player1Card)
            }
        }
    }

    return if (decks[0].size > 0) {
        Pair(0, calculateScore(decks[0]))
    } else {
        Pair(1, calculateScore(decks[1]))
    }
}

private fun calculateScore(deck: List<Int>) =
    deck.reversed().foldIndexed(0) { i, acc, cardValue -> acc + (i + 1) * cardValue }

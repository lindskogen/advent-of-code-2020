package day07

import java.io.File
import kotlin.test.assertEquals
import kotlin.time.measureTimedValue



@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val questions = File("src/main/kotlin/day07/input").readLines()
        val (part1, part2) = parse(questions, "shiny gold")
        assertEquals(part1, 208)
        assertEquals(part2, 1664)
        println(Pair(part1, part2))

    }
    println(elapsed)
}

fun parse(questions: List<String>, id: String): Pair<Int, Int> {
    val childMap = mutableMapOf<String, MutableList<Pair<Int, String>>>()
    val parentMap = mutableMapOf<String, MutableList<String>>()

    questions.forEach {
        var words = it.split(" ")
        val myId = words.take(2).joinToString(" ")
        words = words.drop(4)
        while (words.isNotEmpty()) {
            val noOfBagsString = words.take(1)[0]
            if (noOfBagsString == "no") {
                break
            }
            val noOfBags = noOfBagsString.toInt()
            val bagId = words.drop(1).take(2).joinToString(" ")
            words = words.drop(4)
            childMap.getOrPut(myId, { mutableListOf() }).add(Pair(noOfBags, bagId))
            parentMap.getOrPut(bagId, { mutableListOf() }).add(myId)
        }
    }


    val set = mutableSetOf<String>()
    sumParentBags(parentMap, id, set)
    return Pair(set.size, sumChildrenBags(childMap, id) - 1)
}

fun sumChildrenBags(map: MutableMap<String, MutableList<Pair<Int, String>>>, searchId: String): Int {
    val list = map[searchId]


    return 1 + (list?.sumBy { (c, id) ->
        c * sumChildrenBags(map, id)
    } ?: 0)
}

private fun sumParentBags(
    map2: MutableMap<String, MutableList<String>>,
    currentId: String,
    set: MutableSet<String>
) {
    val list = map2[currentId]
    list?.forEach {
        set.add(it)
        sumParentBags(map2, it, set)
    }
}

//private fun solve(passports: List<String>) {
//    println("Part 1: ${passports.sumOf { countPart1(it) }}")
//    println("Part 2: ${passports.sumOf { countPart2(it) }}")
//}

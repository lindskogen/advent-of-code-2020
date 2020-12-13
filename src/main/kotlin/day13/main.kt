package day13

import java.io.File
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day13/input").readLines()
        val earliestTime = lines[0].toInt()
        val busIds = lines[1].split(',').map { if (it == "x") { null } else { it.toInt() } }
        val p1 = solve1(earliestTime, busIds.filterNotNull())
        println("Part 1: $p1")

        val p2 = solve2(lines[1])
        println("Part 2: $p2")
    }
    println(elapsed)
}


fun solve1(earliestTime: Int, busIds: List<Int>): Int? {
    for (t in earliestTime..Int.MAX_VALUE) {
        for (b in busIds) {
            if (t % b == 0) {
                return (t - earliestTime) * b
            }
        }
    }
    return null
}

fun solve2(input: String): Long {
    val busIds = input.split(',')
        .mapIndexed { index, v -> if (v == "x") { null } else { Pair(v.toInt() - index, v.toInt()) } }.filterNotNull()

    return crt(busIds)
}

fun solve2_2(input: String): Long? {
    val busIds = input.split(',')
        .mapIndexed { index, v -> if (v == "x") { null } else { Pair(v.toInt(), index) } }.filterNotNull()

    val end = 1000000000000000L
    var step = busIds.first().first.toLong()
    var t = step

    var nextBusIndex = 1

    while (t < end) {
        if (busIds.all { (t + it.second) % it.first == 0L }) {
            return t
        }

        val b = busIds[nextBusIndex]

        if ((t + b.second) % b.first == 0L) {
            step *= b.first
            nextBusIndex++
        }
        t += step
    }
    return null
}

private fun crt(pairs: List<Pair<Int, Int>>): Long {
    val N = pairs.fold(1L) { acc, p -> acc * p.second }

    val sum = pairs.sumOf {
        val ni = it.second.toLong()
        val p = N / ni
        val x = multInv(p, ni)

        it.first * x * p
    }

    return sum % N
}


fun multInv(a: Long, b: Long): Long {
    if (b == 1L) return 1
    var aa = a
    var bb = b
    var x0 = 0L
    var x1 = 1L
    while (aa > 1) {
        val q = aa / bb
        var t = bb
        bb = aa % bb
        aa = t
        t = x0
        x0 = x1 - q * x0
        x1 = t
    }
    if (x1 < 0) x1 += b
    return x1
}

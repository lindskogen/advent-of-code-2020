package day14

import java.io.File
import kotlin.math.pow
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day14/input").readLines()
        val p1 = solve1(lines)
        println("Part 1: $p1")

        val p2 = solve2(lines)
        println("Part 2: $p2")
    }
    println(elapsed)
}

fun solve1(lines: List<String>): Long {
    var mask: List<Int?> = listOf()
    val memory = mutableMapOf<Int, String>()

    for (line in lines) {
        if (line.startsWith("mask")) {
            mask = line.substring(7).map { when (it) { '0' -> 0; '1' -> 1; else -> null } }
        } else {
            val parts = line.split('=')
            val addr = parts[0].substring(4).split(']')[0].toInt()
            val value = parts[1].trim().toInt().toString(2).padStart(36, '0')
            memory.compute(addr) { _, mem ->
                val memoryValue = (mem ?: "0".repeat(36)).toCharArray()

                value.forEachIndexed { i, v ->
                    val maskValue = mask[i]
                    if (maskValue == null) {
                        memoryValue[i] = v
                    } else {
                        memoryValue[i] = if (maskValue == 0) { '0' } else {  '1' }
                    }
                }
                memoryValue.joinToString("")
            }
        }
    }

    return memory.values.sumOf { it.toLong(2) }
}


fun solve2(lines: List<String>): Long {
    var mask: List<Int?> = listOf()
    val memory = mutableMapOf<Long, Int>()

    for (line in lines) {
        if (line.startsWith("mask")) {
            mask = line.substring(7).map { when (it) { '0' -> 0; '1' -> 1; else -> null } }
        } else {
            val parts = line.split('=')
            val value = parts[1].trim().toInt()
            var addr = parts[0].substring(4).split(']')[0].toInt()
                .toString(2)
                .padStart(36, '0')
                .toCharArray()
            val xIndices = mutableListOf<Int>()
            addr = addr.mapIndexed { i, v ->
                val maskValue = mask[i]
                if (maskValue == null) {
                    xIndices.add(i)
                    'X'
                } else {
                    if (maskValue == 1 || v == '1') { '1' } else {  '0' }
                }
            }.toCharArray()

            if (xIndices.isEmpty()) {
                val s = addr.joinToString("").toLong(2)
                memory[s] = value
            } else {
                for (c in 0..((2.0).pow(xIndices.size)).toInt()) {
                    val aCopy = addr.copyOf()
                    val cc = c.toString(2).padStart(xIndices.size, '0')
                    xIndices.forEachIndexed { i, v ->
                        aCopy[v] = cc[i]
                    }
                    val s = aCopy.joinToString("").toLong(2)
                    memory[s] = value
                }
            }



        }
    }

    return memory.values.sumOf { it.toLong() }
}


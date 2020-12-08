package day08

import java.io.File
import kotlin.time.measureTimedValue

typealias Instruction = Pair<String, Int>


fun runProgram(program: List<Instruction>, initialAcc: Int): Pair<Boolean, Int> {
    var acc = initialAcc
    var pc = 0
    val set = mutableSetOf<Int>()

    loop@ while (pc < program.size) {
        if (!set.add(pc)) {
            return Pair(false, acc)
        }
        val (op, arg) = program[pc]
        when (op) {
            "nop" -> {
            }
            "acc" -> {
                acc += arg
            }
            "jmp" -> {
                pc += arg
                continue@loop
            }
        }
        pc++
    }
    return Pair(true, acc)
}

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val instructions = File("src/main/kotlin/day08/input").readLines().map(::parseInstruction)

        solve(instructions)

    }
    println(elapsed)
}



private fun parseInstruction(it: String): Instruction {
    val parts = it.split(" ")

    return Pair(parts[0], parts[1].toInt())
}

private fun solve(instructions: List<Instruction>) {
    println("Part 1: ${runProgram(instructions, 0).second}")
    println("Part 2: ${mutateNopJmp(instructions)}")
}

private fun mutateNopJmp(instructions: List<Instruction>): Int? {
    for (i in instructions.indices) {
        val candidate = instructions.toMutableList()
        val (op, arg) = candidate[i]

        val res = when {
            op == "jmp" -> {
                candidate[i] = Pair("nop", arg)
                runProgram(candidate, 0)
            }
            op == "nop" && arg != 0 -> {
                candidate[i] = Pair("jmp", arg)
                runProgram(candidate, 0)
            }
            else -> {
                null
            }
        }

        if (res?.first == true) {
            return res.second
        }
    }

    return null
}

package day18

import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day18/input").readLines()

        println("Part 1: ${solve(lines, false)}")
        println("Part 2: ${solve(lines, true)}")
    }
    println(elapsed)
}

fun hasHigherPrecedence(x: Char, y: Char): Boolean {
    return x != y && x == '+'
}


fun parse(line: String, isPart2: Boolean): List<String> {
    var i = 0
    val line = line.replace(" ", "")
    val list = mutableListOf<String>()
    val stack = Stack<Char>()

    while (i < line.length) {
        val c = line[i]
        when {
            Character.isDigit(c) -> {
                list.add(c.toString())
            }
            c == '*' || c == '+' -> {
                while (stack.isNotEmpty()
                    && (!isPart2 || hasHigherPrecedence(stack.peek(), c))
                    && stack.peek() != '(') {
                    list.add(stack.pop().toString())
                }
                stack.push(c)
            }
            c == '(' -> {
                stack.push(c)
            }
            c == ')' -> {
                while (stack.isNotEmpty() && stack.peek() != '(') {
                    list.add(stack.pop().toString())
                }
                if (stack.isNotEmpty() && stack.peek() == '(') {
                    stack.pop()
                }
            }
        }
        i++
    }

    while (stack.isNotEmpty()) {
        list.add(stack.pop().toString())
    }

    return list
}

fun eval(list: List<String>): Long {
    val list = list.toMutableList()
    val stack = Stack<String>()

    while (list.isNotEmpty()) {
        val c = list.removeFirst()
        when {
            Character.isDigit(c[0]) -> {
                stack.push(c)
            }
            c[0] == '*' -> {
                val o1 = stack.pop().toString().toLong()
                val o2 = stack.pop().toString().toLong()
                stack.push((o1 * o2).toString())
            }
            c[0] == '+' -> {
                val o1 = stack.pop().toString().toLong()
                val o2 = stack.pop().toString().toLong()
                stack.push((o1 + o2).toString())
            }
        }
    }

    return stack.pop().toString().toLong()
}

fun solve(lines: List<String>, isPart2: Boolean): Long {
    return lines.sumOf {
        eval(parse(it, isPart2))
    }
}



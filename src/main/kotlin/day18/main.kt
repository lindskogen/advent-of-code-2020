package day18

import java.io.File
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
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

fun hasHigherPrecedence(x: Char, y: Char): Boolean = x != y && x == '+'


fun parse(line: String, isPart2: Boolean): List<Token> {
    var i = 0
    val line = line.replace(" ", "")
    val list = mutableListOf<Token>()
    val stack = Stack<Char>()

    while (i < line.length) {
        val c = line[i]
        when {
            Character.isDigit(c) -> {
                list.add(Token.Value(c.toString().toLong()))
            }
            c == '*' || c == '+' -> {
                while (stack.isNotEmpty()
                    && (!isPart2 || hasHigherPrecedence(stack.peek(), c))
                    && stack.peek() != '(') {
                    list.add(operatorCharToToken(stack.pop()))
                }
                stack.push(c)
            }
            c == '(' -> {
                stack.push(c)
            }
            c == ')' -> {
                while (stack.isNotEmpty() && stack.peek() != '(') {
                    list.add(operatorCharToToken(stack.pop()))
                }
                if (stack.isNotEmpty() && stack.peek() == '(') {
                    stack.pop()
                }
            }
        }
        i++
    }

    while (stack.isNotEmpty()) {
        list.add(operatorCharToToken(stack.pop()))
    }

    return list
}

private fun operatorCharToToken(c: Char) = when (c) {
    '+' -> Token.Add
    '*' -> Token.Multiply
    else -> throw Exception("No operator implemented for $c")
}

sealed class Token {
    class Value(val num: Long): Token()
    object Add: Token()
    object Multiply: Token()
}



fun eval(list: List<Token>): Long {
    val list = list.toMutableList()
    val stack = Stack<Token>()

    while (list.isNotEmpty()) {
        when (val t = list.removeFirst()) {
            is Token.Value -> {
                stack.push(t)
            }
            Token.Multiply -> {
                val o1 = stack.pop() as Token.Value
                val o2 = stack.pop() as Token.Value
                stack.push(Token.Value(o1.num * o2.num))
            }
            Token.Add -> {
                val o1 = stack.pop() as Token.Value
                val o2 = stack.pop() as Token.Value

                stack.push(Token.Value(o1.num + o2.num))
            }
        }
    }

    return when (val t = stack.pop()) {
        is Token.Value -> t.num
        else -> throw IllegalStateException("Value left on stack is not a value")
    }
}

fun solve(lines: List<String>, isPart2: Boolean): Long {
    return lines.sumOf {
        eval(parse(it, isPart2))
    }
}



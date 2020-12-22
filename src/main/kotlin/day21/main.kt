package day21

import java.io.File
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val lines = File("src/main/kotlin/day21/input").readLines()
        solve(lines)
    }
    println(elapsed)
}

fun solve(foods: List<String>) {
    val allAllergens = mutableSetOf<String>()
    val allIngredients = mutableListOf<String>()
    val foodsList = foods.map { food ->
        val parts = food.removeSuffix(")").split("(contains ")
        val ingredients = parts[0].trim().split(' ').toSet()
        val allergens = parts[1].split(", ")
        allAllergens.addAll(allergens)
        allIngredients.addAll(ingredients)
        allergens.map { it to ingredients }.toMap().toMutableMap()
    }

    val allIngredientsWithAllergens = allAllergens.map { allergen ->
        val allFoodsWithAllergen = foodsList.mapNotNull { food -> food[allergen] }
        val single = allFoodsWithAllergen.reduce { acc, set ->
            acc.intersect(set)
        }
        allergen to single
    }.toMap()



    allIngredients.removeAll(allIngredientsWithAllergens.values.flatten())


    val count = allIngredients.count()

    println("Part 1: $count")

    val uniqueIngredientPerAllergen = allIngredientsWithAllergens.mapValues { entry ->
        entry.value.toMutableSet()
    }

    var changed: Boolean
    do {
        changed = false
        for (allergen in uniqueIngredientPerAllergen) {
            if (allergen.value.size == 1) {
                val uniqueAllergen = allergen.value.first()
                uniqueIngredientPerAllergen.forEach { entry ->
                    if (entry.key != allergen.key) {
                        changed = entry.value.remove(uniqueAllergen) || changed
                    }
                }
            }
        }
    } while (changed)


    val part2 = uniqueIngredientPerAllergen.keys.sorted().map {
        uniqueIngredientPerAllergen[it]?.singleOrNull()
    }.joinToString(",")

    println("Part 2: $part2")
}

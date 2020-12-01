import java.io.File
import kotlin.time.measureTimedValue

@kotlin.time.ExperimentalTime
fun main(args: Array<String>) {
    val (value, elapsed) = measureTimedValue {
        val nums = File("src/main/kotlin/day01/input").readLines().map(Integer::parseInt)
        solve(nums)
    }
    println(elapsed)
}

private fun solve(nums: List<Int>) {
    var found2 = false
    var found3 = false

    for (i in nums.indices) {
        for (j in i until nums.size) {
            if (nums[i] + nums[j] == 2020) {
                println("Found 2: ${nums[i] * nums[j]}")
                found2 = true
                if (found3) {
                    return
                }
            }

            for (k in j until nums.size) {
                if (nums[i] + nums[j] + nums[k] == 2020) {
                    println("Found 3: ${nums[i] * nums[j] * nums[k]}")
                    found3 = true
                    if (found2) {
                        return
                    }
                }
            }
        }
    }
}

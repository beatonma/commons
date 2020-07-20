package org.beatonma.commons.debug

private val iterationTracker: MutableMap<String, Tracker> = mutableMapOf()
private class Tracker(var totalDuration: Long) {
    var count: Int = 0
}
fun benchmarkReportComplete(tag: String, duration: Long, reportFrequency: Int) {
    if (iterationTracker.containsKey(tag)) {
        iterationTracker[tag]!!.apply {
            count++
            totalDuration += duration
            if (count >= reportFrequency) {
                println("AVG_BENCHMARK ${totalDuration / count}ms [$tag: $reportFrequency iterations, totalDuration=${totalDuration}ms]")
                count = 0
                totalDuration = 0
            }
        }
    }
    else {
        iterationTracker[tag] = Tracker(duration)
    }
}

inline fun benchmark(tag: String? = null, block: () -> Any?) {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    println("BENCHMARK ${end - start}ms ${if (tag == null) "" else " [$tag]"}")
}

inline fun <reified T> T.benchmark(tag: String? = null, block: T.() -> Any?) {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    println("BENCHMARK ${end - start}ms ${if (tag == null) "" else " [$tag]"}")
}

/**
 * Useful for onDraw or similar functions that run frequently - only report benchmark values
 * every [reportFrequency] times it is run.
 */
inline fun meanBenchmark(reportFrequency: Int = 10, tag: String, block: () -> Any?) {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    benchmarkReportComplete(tag, end - start, reportFrequency)
}

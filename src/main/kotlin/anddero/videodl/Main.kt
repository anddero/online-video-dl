package anddero.videodl

import java.time.Instant
import java.time.temporal.ChronoUnit

//const val PREV_LINE_ESCAPE_CODE = "\u0033[F"
//const val LINE_START_ESCAPE_CODE = "\r"
const val MAX_PROCESS_COUNT = 5
const val PYTHON_COMMAND = "python3"

fun main() { // TODO Thread pool
    var completeCount = 0

    val queue = VideoListReader.readVideoList().toMutableList()
    val totalCount = queue.size

    val runningProcessList = mutableListOf<Pair<ViewLastLineStream, Process>>()

    val startTime = Instant.now()

    while (queue.isNotEmpty() && runningProcessList.size < MAX_PROCESS_COUNT) {
        val next = queue.removeFirst()
        runningProcessList.add(startDownloadMp4VideoProcess(next.first, next.second))
    }

    while (queue.isNotEmpty() || runningProcessList.any { it.second.isAlive }) {
        for (i in 0 until runningProcessList.size) {
            if (!runningProcessList[i].second.isAlive) {
                if (queue.isNotEmpty()) {
                    val removedPair = runningProcessList.removeAt(i)
                    removedPair.first.thread.interrupt()
                    ++completeCount
                    val next = queue.removeFirst()
                    runningProcessList.add(i, startDownloadMp4VideoProcess(next.first, next.second))
                } else {
                    runningProcessList[i].first.lastLine = "<complete>"
                }
            }
        }
        printState(completeCount, totalCount, runningProcessList, startTime)
        Thread.sleep(5000)
    }
    printState(completeCount, totalCount, runningProcessList, startTime)

    println()
    println()
    println("All finished!")
}

fun printState(
    completeCount: Int,
    totalCount: Int,
    runningProcessList: List<Pair<ViewLastLineStream, Process>>,
    startTime: Instant
) {
    val elapsedTimeMs = startTime.until(Instant.now(), ChronoUnit.MILLIS)
    val percent = if (totalCount == 0) 100 else completeCount * 100 / totalCount
    var etaMs = if (completeCount == 0) 0 else elapsedTimeMs * (totalCount - completeCount) / completeCount
    var etaS = etaMs / 1000
    etaMs -= etaS * 1000
    var etaM = etaS / 60
    etaS -= etaM * 60
    val etaH = etaM / 60
    etaM -= etaH * 60

    println()
    println()
    println()
    println()
    println("Complete: $completeCount/$totalCount = $percent% // Threads ${ViewLastLineStream.activeThreadCount}" +
            " // Eta: ${etaH}h ${etaM.toString().padStart(2, '0')}:${etaS.toString().padStart(2, '0')}")
    runningProcessList.forEachIndexed { index, it ->
        println("Thread ${index + 1}: ${it.first.lastLine}")
    }
}

fun startDownloadMp4VideoProcess(outVideoFileName: String, srcMpdFileUrl: String): Pair<ViewLastLineStream, Process> {
    val builder = ProcessBuilder(
        PYTHON_COMMAND,
        "-m", "youtube_dl",
        "--merge-output-format", "mp4",
        "-f", "worstvideo+worstaudio",
        "-o", outVideoFileName,
        srcMpdFileUrl
    )
    builder.redirectErrorStream(true)
    val process = builder.start()
    return Pair(ViewLastLineStream(process.inputStream), process)
}

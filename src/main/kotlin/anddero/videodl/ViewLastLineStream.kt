package anddero.videodl

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class ViewLastLineStream(inputStream: InputStream) {
    private val reader = BufferedReader(InputStreamReader(inputStream))
    val thread = Thread { run() }

    var lastLine = "<waiting for output...>"
        @Synchronized set
        @Synchronized get

    init {
        thread.start()
        activeThreadCount++
    }

    private fun run() {
        while (!Thread.interrupted()) {
            if (!readLine()) {
                break
            }
        }
        try {
            reader.close()
        } catch (e: IOException) {
        } finally {
            activeThreadCount--
        }
    }

    private fun readLine(): Boolean {
        lastLine = reader.readLine() ?: return false
        return true
    }

    companion object {
        var activeThreadCount = 0
            @Synchronized get
            @Synchronized set
    }
}

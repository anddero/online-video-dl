package anddero.videodl

import org.yaml.snakeyaml.Yaml
import java.lang.RuntimeException

class VideoListReader {
    private fun read(): List<Pair<String, String>> {
        val yaml = Yaml()
        val videos: Map<String, Any> = yaml.load(VideoListReader::class.java.getResourceAsStream(VIDEO_LIST_FILE_NAME))
        return videos[VIDEO_LIST_KEY]?.let { flattenCourses(it) } ?: emptyList()
    }

    private fun flattenCourses(it: Any) =
        (it as Map<*, *>).flatMap { flattenRecursively(it.key as String, it.value!!) }
            .map { Pair(it.first + ".mp4", it.second) }

    private fun flattenRecursively(key: String, value: Any): List<Pair<String, String>> {
        return when (value) {
            is Map<*, *> -> value.flatMap { flattenRecursively(it.key as String, it.value!!) }
            is List<*> -> value.mapIndexed { index, link -> Pair("Video ${index + 1}", link as String) }
            else -> throw RuntimeException("Unrecognized node with key [$key] and value type [${value.javaClass.name}]")
        }.map { Pair("$key - ${it.first}", it.second) }
    }

    companion object {
        private const val VIDEO_LIST_FILE_NAME = "/courses.yml"
        private const val VIDEO_LIST_KEY = "courses"
        fun readVideoList() = VideoListReader().read()
    }
}

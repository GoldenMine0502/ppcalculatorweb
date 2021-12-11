package kr.goldenmine.ppcalculatorweb

import kr.goldenmine.ppcalculatorweb.core.BeatmapCalculator
import java.io.File

fun main() {
//    val folder = File("D:/Develop/osu/replays")
//    val replay = File(folder, "3333745/replay-osu_3333745_3969709534.osr")
//    val map = File(folder, "3333745.osu")
//
//    val beatmapCalculator = BeatmapCalculator(map, replay)

    val beatmapCalculator = BeatmapCalculator(File("test/map.osu"), File("test/replay2.osr"), 0)

    println(beatmapCalculator.timings)
}
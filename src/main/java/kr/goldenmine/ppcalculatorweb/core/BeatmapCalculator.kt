package kr.goldenmine.ppcalculatorweb.core

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kr.goldenmine.ppcalculatorweb.core.osu.loadBeatmap
import kr.goldenmine.ppcalculatorweb.core.replay.Score
import kr.goldenmine.ppcalculatorweb.util.Point
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

private val gson = Gson()

class BeatmapCalculator(beatmapFile: File, replayFile: File, private val mods: Int) {
    val beatmap = loadBeatmap(beatmapFile)
    val score: Score

    val timings = mutableListOf<Double>()

    init {
        // read replay from exe
        val p = Runtime.getRuntime().exec("./lib/PerformanceCalculator.exe parse \"${beatmapFile.path}\" \"${replayFile.path}\"")
        val type = object : TypeToken<Array<ReplayEventOsu>>() {}.type
        val text = BufferedReader(InputStreamReader(p.inputStream)).readText()

        println(text)
        score = gson.fromJson<Score>(text, type)
        p.destroy()

        findTimings()
    }

    private fun findTimings() {
        var previousOffset = 0
        var previousPosition: Point? = null

//        println("size: ${replayEvents.size} $index ${beatmap.hitObjects.size}")

        val hitObjects = beatmap.getHitObjectsWithMods(mods)

        for(frame in score.replay.frames) {
            val currentOffset = frame.time.toInt()
            val position = frame.position.toPoint()
//            val currentOffset = previousOffset + replayEvent.timeDelta

            if(previousPosition != null) {
                val hitObject = hitObjects.firstOrNull { it.startOffset in previousOffset..currentOffset }
                if (hitObject != null) {
                    val percent = (hitObject.startOffset - previousOffset) / (currentOffset - previousOffset)

                    val currentPosition = Point(position.x, position.y)

                    val estimatedPosition = previousPosition + (currentPosition - previousPosition) * percent
                    val objectPosition = hitObject.startPosition

                    timings.add((objectPosition - estimatedPosition).length)
                }
            }
            previousPosition = position
            previousOffset += currentOffset
        }

//        println(beatmap.hitObjects.map { it.startOffset }.joinToString { it.toString() })

//        for (hitObject in beatmap.hitObjects) {
//            while(true) {
////                println("$index")
//                if(replayEvents.size <= index + 1) break
//
//                val currentEvent = replayEvents[index]
//                val nextEvent = replayEvents[index + 1]
//
//                val currentOffset = previousOffset + nextEvent.timeDelta
//
//                println("index: $index hitobject: ${hitObject.startOffset}, previousOffset: $previousOffset, currentoffset: $currentOffset, delta: ${nextEvent.timeDelta}")
//
//                if(hitObject.startOffset == currentOffset) {
//                    val distance = (hitObject.startPosition - Point(currentEvent.x, currentEvent.y)).length
//                    timings.add(distance)
//                    println("same offset")
//                    break
//                } else if(hitObject.startOffset > currentOffset) {
//                    if(replayEvents.size <= index + 2) break
//
//                    val nextnextEvent = replayEvents[index + 2]
//
//                    val nextOffset = currentOffset + nextnextEvent.timeDelta
//
//                    println("nextOffset: $nextOffset")
//
//                    if(hitObject.startOffset in currentOffset..nextOffset) {
//                        val percentage = (hitObject.startOffset - currentOffset) / (nextOffset - currentOffset)
//
//                        val nextPosition = Point(nextEvent.x, nextEvent.y)
//                        val nextnextPosition = Point(nextnextEvent.x, nextnextEvent.y)
//
//                        val estimatedPosition = nextPosition + (nextnextPosition - nextPosition) * percentage
//
//                        val distance = (hitObject.startPosition - estimatedPosition).length
//                        timings.add(distance)
//
//                        println("different offset")
//                        break
//                    }
//                }
//                previousOffset += currentEvent.timeDelta
//                index++
//            }
//        }

    }
}


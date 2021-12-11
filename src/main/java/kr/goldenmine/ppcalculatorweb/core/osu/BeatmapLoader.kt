package kr.goldenmine.ppcalculatorweb.core.osu

import kr.goldenmine.ppcalculatorweb.util.Point
import kr.goldenmine.ppcalculatorweb.util.calculateBPM
import java.io.File
import java.lang.RuntimeException
import java.util.regex.Pattern


enum class BeatmapType(val type: String, val colon: Boolean) {
    None("", false),
    General("[General]", true),
    Editor("[Editor]", true),
    Metadata("[Metadata]", true),
    Difficulty("[Difficulty]", true),
    Events("[Events]", false),
    BreakPeriods("//Break Periods", false),
    TimingPoints("[TimingPoints]", false),
    HitObjects("[HitObjects]", false),
}

enum class BeatmapAttribute(val type: String) {
    Title("Title"),
    TitleUnicode("TitleUnicode"),
    Artist("Artist"),
    ArtistUnicode("ArtistUnicode"),
    Version("Version"),
    BeatmapId("BeatmapID"),
    BeatmapSetId("BeatmapSetID"),
    HP("HPDrainRate"),
    CS("CircleSize"),
    AR("ApproachRate"),
    OD("OverallDifficulty"),
    SliderMultiplier("SliderMultiplier"),

}

val splitPattern = Pattern.compile(":[ ]?")

fun loadBeatmap(route: File): Beatmap {
    var titleUnicode: String? = null
    var title: String? = null
    var artistUnicode: String? = null
    var artist: String? = null
    var version: String? = null
    var beatmapId: Int? = null
    var beatmapSetId: Int? = null
    var HP: Double? = null
    var AR: Double? = null
    var CS: Double? = null
    var OD: Double? = null
    var sliderVelocity: Double? = null
    val timingPoints = ArrayList<TimingPoint>()
    val hitObjects = ArrayList<HitObject>()
    val breakPeriods = ArrayList<BreakPeriod>()

    route.useLines { lines ->
        var lastType = BeatmapType.None
        var lastbpm: Double? = null
        var lastbpmMs: Double? = null
        lines.forEach line@{ line ->
            //println(line)

            BeatmapType.values().forEach {
                if (it.type == line) {
                    lastType = it
                    return@line
                }
            }

            if (lastType.colon) {
                val (key, value) = line.split(splitPattern)

                when (BeatmapAttribute.values().firstOrNull { it.type == key }) {
                    BeatmapAttribute.Title -> title = value
                    BeatmapAttribute.TitleUnicode -> titleUnicode = value
                    BeatmapAttribute.Artist -> artist = value
                    BeatmapAttribute.ArtistUnicode -> artistUnicode = value
                    BeatmapAttribute.Version -> version = value
                    BeatmapAttribute.BeatmapId -> beatmapId = value.toInt()
                    BeatmapAttribute.BeatmapSetId -> beatmapSetId = value.toInt()
                    BeatmapAttribute.HP -> HP = value.toDouble()
                    BeatmapAttribute.CS -> CS = value.toDouble()
                    BeatmapAttribute.AR -> AR = value.toDouble()
                    BeatmapAttribute.OD -> OD = value.toDouble()
                    BeatmapAttribute.SliderMultiplier -> sliderVelocity = value.toDouble()
                }
            } else {
                when (lastType) {
                    BeatmapType.TimingPoints -> {
                        val defaultVelocity = sliderVelocity ?: throw RuntimeException("no slider velocity")

                        val split = line.split(",")
                        val offset = split[0].toDouble()
                        val metronome = split[2].toInt()
                        val inherited = split[6] == "0"
                        val bpm: Double
                        val bpmMs: Double
                        val sliderVelocity: Double

                        if (inherited) {
                            bpmMs = lastbpmMs ?: throw RuntimeException("inherited but lastbpm is null")
                            bpm = lastbpm ?: throw RuntimeException("inherited but lastbpm is null")
                            sliderVelocity = defaultVelocity * (-100.0 / split[1].toDouble()) // 귀납적
                        } else {
                            bpmMs = split[1].toDouble()
                            bpm = calculateBPM(split[1].toDouble())
                            sliderVelocity = defaultVelocity
                        }

                        val timingPoint = TimingPoint(offset, bpm, bpmMs, sliderVelocity, metronome, inherited)
                        timingPoints.add(timingPoint)

                        lastbpm = timingPoint.bpm
                        lastbpmMs = bpmMs
                    }
                    BeatmapType.BreakPeriods -> {

                    }
                    BeatmapType.HitObjects -> {
                        val split = line.split(",")
                        val point = Point(split[0].toInt(), split[1].toInt())
                        val offset = split[2].toInt()
                        val timingPoint = timingPoints.first { offset >= it.offset }
                        val objectData = if(split.size >= 6) split[5] else null
                        val hitObject: HitObject


                        when {
                            objectData != null && objectData.contains("|") -> { // slider
                                //x,y,time,type,hitSound,curveType|curvePoints,slides,length,edgeSounds,edgeSets,hitSample
                                val dataSplited = objectData.split("|").toMutableList()
                                val type = dataSplited.removeAt(0)
                                val points = dataSplited.map {
                                    val (x, y) = it.split(":")
                                    Point(x.toInt(), y.toInt())
                                }
                                val reverseCount = split[6].toInt()
                                val length = split[7].toDouble()

                                val dots = ArrayList<SliderDot>()
                                dots.add(SliderDot(point, DotType.NONE))

                                var index = 0
                                while (index < points.size) {
                                    if (index < points.size - 1 && points[index] == points[index + 1]) {
                                        dots.add(SliderDot(points[index], DotType.STRAIGHT))
                                        index++
                                    } else {
                                        dots.add(SliderDot(points[index], DotType.CURVE))
                                    }
                                    index++
                                }

                                val sliderType = Slider.Type.values().first { it.chracter == type }

                                //println("x")
                                // TODO need to calculate finishOffset
                                hitObject = Slider(
                                        dots,
                                        offset,
                                        timingPoint,
                                        length,
                                        reverseCount,
                                        // for 3 dots straight slider
                                        if(sliderType == Slider.Type.CURVE && dots.size != 3) Slider.Type.BEZIER else sliderType)
                            }
                            objectData == null || objectData.contains(":") -> { // circle
                                hitObject = Circle(point, offset)
                            }
                            else -> { // spinner
                                val endOffset = objectData.toInt()
                                hitObject = Spinner(offset, endOffset)
                            }
                        }

                        hitObjects.add(hitObject)
                    }
                }
            }
        }
    }

    return Beatmap(
            title = title ?: throw RuntimeException("no title"),
            titleUnicode = titleUnicode ?: throw RuntimeException("no titleUnicode"),
            artist = artistUnicode ?: throw RuntimeException("no artistUnicode"),
            artistUnicode = artistUnicode ?: throw RuntimeException("no artistUnicode"),
            version = version ?: throw RuntimeException("no version"),
            beatmapId = beatmapId ?: throw RuntimeException("no beatmapId"),
            beatmapSetId = beatmapSetId ?: throw RuntimeException("no beatmapSetId"),
            HP = HP ?: throw RuntimeException("no HP"),
            CS = CS ?: throw RuntimeException("no CS"),
            AR = AR ?: throw RuntimeException("no AR"),
            OD = OD ?: throw RuntimeException("no OD"),
            baseSliderVelocity = sliderVelocity ?: throw RuntimeException("no baseSliderVelocity"),
            timingPoints = timingPoints,
            hitObjects = hitObjects
    )
}
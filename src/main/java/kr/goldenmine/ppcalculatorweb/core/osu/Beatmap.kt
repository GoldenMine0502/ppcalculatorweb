package kr.goldenmine.ppcalculatorweb.core.osu

import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuUtil
import kr.goldenmine.ppcalculatorweb.util.Point

private val baseSize = Point(512, 384)

class Beatmap(
        val title: String,
        val titleUnicode: String,
        val artist: String,
        val artistUnicode: String,
//    val creator: String
        val version: String,
//    val source: String
//    val tags: List<String>
        val beatmapId: Int,
        val beatmapSetId: Int,
        val HP: Double,
        val CS: Double,
        val OD: Double,
        val AR: Double,
        val baseSliderVelocity: Double,
        //val sliderTickRate: Int,
        val timingPoints: List<TimingPoint>,
        val hitObjects: List<HitObject>
): IAttribute {

    val attributesPrivate = HashMap<String, Any>()

    override fun getAttributes(): HashMap<String, Any> = attributesPrivate
    override fun addAttribute(key: String, value: Any) {
        attributesPrivate[key] = value
    }

    override fun getAttribute(key: String): Any? {
        return attributesPrivate[key]
    }

    fun findTimingPoint(offset: Int): TimingPoint {
        return timingPoints.firstOrNull() { it.offset >= offset } ?: timingPoints[0]
    }

    fun getHitObjectsWithMods(mods: Int): List<HitObject> {
        val newerHitObjects = ArrayList<HitObject>()

        hitObjects.forEach {
            var x = it.startPosition.x
            var y = it.startPosition.y
            var offset = it.startOffset
            var finishOffset = it.finishOffset

            if((mods and OsuUtil.ModsStandard.HardRock.type) > 0) {
                y = baseSize.y - y
            }

            if((mods and OsuUtil.ModsStandard.DoubleTime.type) > 0) {
                offset = (offset * 2.0 / 3.0).toInt()
                finishOffset = (finishOffset * 2.0 / 3.0).toInt()
            }

            if((mods and OsuUtil.ModsStandard.HalfTime.type) > 0) {
                offset = (offset * 4.0 / 3.0).toInt()
                finishOffset = (finishOffset * 4.0 / 3.0).toInt()
            }

            val result = when (it) {
                is Circle -> {
                    Circle(Point(x, y), offset)
                }
                is Slider -> {
                    // TODO apply clockRate to timingPoint, flip slider points on hardrock
                    Slider(it.points, offset, it.timingPoint, it.lengthPixel, it.reverseCount, it.type) as HitObject
                }
                else -> {
                    Spinner(offset, finishOffset)
                }
            }

            newerHitObjects.add(result)
        }

        return hitObjects
    }
}

const val lastOffset = 10000

val Beatmap.length
    get() = hitObjects.last().finishOffset + lastOffset

fun Beatmap.convertARtoMs(mods: Int = 0): Double {
    var ARMods = AR

    if(mods and OsuUtil.ModsStandard.HardRock.type > 0) {
        ARMods *= 1.4
        if(ARMods > 10) ARMods = 10.0
    }
    if(mods and OsuUtil.ModsStandard.Easy.type > 0) {
        ARMods *= 0.5
    }

    var ms = when {
        ARMods < 5 -> 1200.0 + 600.0 * (5 - ARMods) / 5
        ARMods == 5.0 -> 1200.0
        else -> 1200.0 - 750.0 * (ARMods - 5) / 5
    }

    if(mods and OsuUtil.ModsStandard.DoubleTime.type > 0) {
        ms *= 2.0 / 3.0
    }
    if(mods and OsuUtil.ModsStandard.HalfTime.type > 0) {
        ms *= 4.0 / 3.0
    }

    return ms
}

fun Beatmap.convertCStoRadius(mods: Int = 0): Double {
    var multiplier = 1.0
    if(mods and OsuUtil.ModsStandard.HardRock.type > 0)
        multiplier *= 1.4
    if(mods and OsuUtil.ModsStandard.Easy.type > 0)
        multiplier *= 0.5
//        if(mods and Mods.HR.value > 0)
    return 54.4 - 4.48 * CS * multiplier
}

fun Beatmap.calculateODPre(mods: Int): Double {
    var ODMods = OD

    if(mods and OsuUtil.ModsStandard.HardRock.type > 0) {
        ODMods *= 1.4
        if(ODMods > 10) ODMods = 10.0
    }
    if(mods and OsuUtil.ModsStandard.Easy.type > 0) {
        ODMods *= 0.5
    }

    return ODMods
}

private fun Beatmap.calculateODPost(ms: Double, mods: Int): Double {
    var ms = ms

    if(mods and OsuUtil.ModsStandard.DoubleTime.type > 0) {
        ms *= 2.0 / 3.0
    }
    if(mods and OsuUtil.ModsStandard.HalfTime.type > 0) {
        ms *= 4.0 / 3.0
    }

    return ms
}

/*
50	400ms - 20ms * OD
100	280ms - 16ms * OD
300	160ms - 12ms * OD
 */

fun Beatmap.convertODto300(mods: Int = 0): Double {
    return calculateODPost(160 - 12 * calculateODPre(mods), mods)
}
fun Beatmap.convertODto100(mods: Int = 0): Double {
    return calculateODPost(280 - 16 * calculateODPre(mods), mods)
}
fun Beatmap.convertODto50(mods: Int = 0): Double {
    return calculateODPost(400 - 20 * calculateODPre(mods), mods)
}
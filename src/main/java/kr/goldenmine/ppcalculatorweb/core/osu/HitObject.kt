package kr.goldenmine.ppcalculatorweb.core.osu

import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuUtil
import kr.goldenmine.ppcalculatorweb.util.Point

interface HitObject: IAttribute {
    val startOffset: Int
    val finishOffset: Int
    val startPosition: Point
    val endPosition: Point
//    fun getDeltaTime(other: Int): Int
//    fun getStrainTime(other: Int) = getDeltaTime(other).coerceAtLeast(50)
}
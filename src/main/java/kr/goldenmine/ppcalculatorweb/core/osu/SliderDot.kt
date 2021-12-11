package kr.goldenmine.ppcalculatorweb.core.osu

import kr.goldenmine.ppcalculatorweb.util.Point

enum class DotType {
    NONE, CURVE, STRAIGHT
}

data class SliderDot(
        val point: Point,
        val dotType: DotType
)
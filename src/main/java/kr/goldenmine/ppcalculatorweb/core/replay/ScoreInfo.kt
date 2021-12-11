package kr.goldenmine.ppcalculatorweb.core.replay

import com.google.gson.annotations.SerializedName
import java.util.*

class ScoreInfo {
    @SerializedName("ID")
    val id = 0

    @SerializedName("Rank")
    val rank = 0

    @SerializedName("TotalScore")
    val totalScore: Long = 0

    @SerializedName("Accuracy")
    val accuracy = 0.0

    @SerializedName("PP")
    val pp: Double? = 0.0

    @SerializedName("MaxCombo")
    val maxCombo = 0

    @SerializedName("Combo")
    val combo = 0

    @SerializedName("RulesetID")
    val rulesetId = 0

    @SerializedName("Passed")
    val passed = false

    @SerializedName("APIMods")
    val mods: Array<Mod> = arrayOf()

    @SerializedName("UserString")
    val userName = ""

    @SerializedName("UserID")
    val userId = 0

    @SerializedName("BeatmapInfoID")
    var beatmapInfoId = 0

    @SerializedName("OnlineScoreID")
    val onlineScoreId: Int? = 0

    @SerializedName("Date")
    val date = ""

    @SerializedName("Statistics")
    val statistics: HashMap<String, Int> = HashMap()

    @SerializedName("OnlineID")
    val onlineId = 0


}
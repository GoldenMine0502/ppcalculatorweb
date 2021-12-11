package kr.goldenmine.ppcalculatorweb.core.replay;

import com.google.gson.annotations.SerializedName;

public class Score {
    @SerializedName("ScoreInfo")
    private ScoreInfo scoreInfo;

    @SerializedName("Replay")
    private OsuReplay replay;

    public ScoreInfo getScoreInfo() {
        return scoreInfo;
    }

    public OsuReplay getReplay() {
        return replay;
    }
}

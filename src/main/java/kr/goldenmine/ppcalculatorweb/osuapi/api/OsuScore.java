package kr.goldenmine.ppcalculatorweb.osuapi.api;

import com.google.gson.annotations.SerializedName;

public class OsuScore {

    @SerializedName("beatmap_id")
    private int beatmapId;

    @SerializedName("score_id")
    private long scoreId;

    @SerializedName("score")
    private long score;

    @SerializedName("maxcombo")
    private int maxcombo;

    @SerializedName("count50")
    private int count50;

    @SerializedName("count100")
    private int count100;

    @SerializedName("count300")
    private int count300;

    @SerializedName("countmiss")
    private int countMiss;

    @SerializedName("countkatu")
    private int countKatu;

    @SerializedName("countgeki")
    private int countGeki;

    @SerializedName("perfect")
    private int perfect;

    @SerializedName("enabled_mods")
    private int enabledMods;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("date")
    private String date;

    @SerializedName("rank")
    private String rank;

    @SerializedName("pp")
    private double pp;

    private int replay_avilable;


    public int getBeatmapId() {
        return beatmapId;
    }

    public long getScoreId() {
        return scoreId;
    }

    public long getScore() {
        return score;
    }

    public int getMaxcombo() {
        return maxcombo;
    }

    public int getCount50() {
        return count50;
    }

    public int getCount100() {
        return count100;
    }

    public int getCount300() {
        return count300;
    }

    public int getCountgeki() {
        return countGeki;
    }

    public int getCountkatu() {
        return countKatu;
    }

    public int getCountmiss() {
        return countMiss;
    }

    public int getPerfect() {
        return perfect;
    }

    public int getEnabledMods() {
        return enabledMods;
    }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getRank() {
        return rank;
    }

    public double getPerformancePoint() {
        return pp;
    }

    public int getReplay_avilable() {
        return replay_avilable;
    }
}

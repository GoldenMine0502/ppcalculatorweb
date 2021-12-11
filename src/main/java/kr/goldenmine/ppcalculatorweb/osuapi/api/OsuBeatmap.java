package kr.goldenmine.ppcalculatorweb.osuapi.api;

import com.google.gson.annotations.SerializedName;

public class OsuBeatmap {
    @SerializedName("approved")
    public String approved;

    @SerializedName("submit_date")
    public String submitDate;

    @SerializedName("approved_date")
    public String approvedDate;

    @SerializedName("last_update")
    public String lastUpdate;

    @SerializedName("artist")
    public String artist;

    @SerializedName("beatmap_id")
    public String beatmapId;

    @SerializedName("beatmapset_id")
    public String beatmapsetId;

    @SerializedName("bpm")
    public String bpm;

    @SerializedName("creator")
    public String creator;

    @SerializedName("creator_id")
    public String creatorId;

    @SerializedName("difficultyrating")
    public String starRating;

    @SerializedName("diff_aim")
    public String aimRating;

    @SerializedName("diff_speed")
    public String speedRating;

    @SerializedName("diff_size")
    public String CS;

    @SerializedName("diff_overall")
    public String OD;

    @SerializedName("diff_approach")
    public String AR;

    @SerializedName("diff_drain")
    public String HP;

    @SerializedName("hit_length")
    public String hitLength;

    @SerializedName("source")
    public String source;

    @SerializedName("genre_id")
    public String genreId;

    @SerializedName("language_id")
    public String languageId;

    @SerializedName("title")
    public String title;

    @SerializedName("total_length")
    public String totalLength;

    @SerializedName("version")
    public String version;

    @SerializedName("file_md5")
    public String md5;

    @SerializedName("mode")
    public String mode;

    @SerializedName("tags")
    public String tags;

    @SerializedName("favourite_count")
    public String favoriteCount;

    @SerializedName("rating")
    public String rating;

    @SerializedName("playcount")
    public String playCount;

    @SerializedName("passcount")
    public String passCount;

    @SerializedName("count_normal")
    public String amountCircle;

    @SerializedName("count_slider")
    public String amountSlider;

    @SerializedName("count_spinner")
    public String amountSpinner;

    @SerializedName("max_combo")
    public String maxCombo;

    @SerializedName("storyboard")
    public String storyboard;

    @SerializedName("video")
    public String video;

    @SerializedName("download_unavailable")
    public String downloadUnavailable;

    @SerializedName("audio_unavailable")
    public String audioUnavailable;

}

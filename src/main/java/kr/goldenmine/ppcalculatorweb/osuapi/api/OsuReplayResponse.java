package kr.goldenmine.ppcalculatorweb.osuapi.api;

import com.google.gson.annotations.SerializedName;

public class OsuReplayResponse {
    @SerializedName("content")
    private String content;

    @SerializedName("encoding")
    private String encodingType;

    public String getContent() {
        return content;
    }

    public String getEncodingType() {
        return encodingType;
    }
}

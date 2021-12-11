package kr.goldenmine.ppcalculatorweb.core.replay;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Mod {
    @SerializedName("Acronym")
    private String acronym;

    @SerializedName("Settings")
    private HashMap<String, Object> settings;

    public String getAcronym() {
        return acronym;
    }

    public HashMap<String, Object> getSettings() {
        return settings;
    }
}

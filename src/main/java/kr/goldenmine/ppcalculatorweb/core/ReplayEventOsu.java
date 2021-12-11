package kr.goldenmine.ppcalculatorweb.core;

import com.google.gson.annotations.SerializedName;

public class ReplayEventOsu {
    int keys;

    @SerializedName("time_delta")
    int timeDelta;

    double x;

    double y;

    public int getKeys() {
        return keys;
    }

    public int getTimeDelta() {
        return timeDelta;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(x: " + x + ", y: " + y + ", timeDelta: " + timeDelta + ", keys: " + keys + ")";
    }
}
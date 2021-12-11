package kr.goldenmine.ppcalculatorweb.core.replay;

import com.google.gson.annotations.SerializedName;
import kr.goldenmine.ppcalculatorweb.util.Point;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;

import java.util.List;

public class OsuReplayFrame {

    @SerializedName("Position")
    private Vector2 position;

    @SerializedName("Actions")
    private int[] actions;

    @SerializedName("Time")
    private double time;

    public Vector2 getPosition() {
        return position;
    }

    public int[] getActions() {
        return actions;
    }

    public double getTime() {
        return time;
    }
}

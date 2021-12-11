package kr.goldenmine.ppcalculatorweb.core.replay;

import com.google.gson.annotations.SerializedName;
import kr.goldenmine.ppcalculatorweb.util.Point;

public class Vector2 {

    @SerializedName("X")
    private float x;

    @SerializedName("Y")
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Point toPoint() {
        return new Point(x, y);
    }
}

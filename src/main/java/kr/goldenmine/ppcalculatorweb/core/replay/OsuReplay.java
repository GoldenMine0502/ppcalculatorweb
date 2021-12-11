package kr.goldenmine.ppcalculatorweb.core.replay;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OsuReplay {
    @SerializedName("HasReceivedAllFrames")
    private boolean receivedAllFrames;

    @SerializedName("Frames")
    private OsuReplayFrame[] frames;

    public boolean isreceivedAllFrames() {
        return receivedAllFrames;
    }

    public OsuReplayFrame[] getFrames() {
        return frames;
    }
}

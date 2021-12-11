package kr.goldenmine.ppcalculatorweb.osuapi.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OsuUtil {
//    None           = 0,
//    NoFail         = 1,
//    Easy           = 2,
//    TouchDevice    = 4,
//    Hidden         = 8,
//    HardRock       = 16,
//    SuddenDeath    = 32,
//    DoubleTime     = 64,
//    Relax          = 128,
//    HalfTime       = 256,
//    Nightcore      = 512, // Only set along with DoubleTime. i.e: NC only gives 576
//    Flashlight     = 1024,
//    Autoplay       = 2048,
//    SpunOut        = 4096,
//    Relax2         = 8192,    // Autopilot
//    Perfect        = 16384, // Only set along with SuddenDeath. i.e: PF only gives 16416
//    Key4           = 32768,
//    Key5           = 65536,
//    Key6           = 131072,
//    Key7           = 262144,
//    Key8           = 524288,
//    FadeIn         = 1048576,
//    Random         = 2097152,
//    Cinema         = 4194304,
//    Target         = 8388608,
//    Key9           = 16777216,
//    KeyCoop        = 33554432,
//    Key1           = 67108864,
//    Key3           = 134217728,
//    Key2           = 268435456,
//    ScoreV2        = 536870912,
//    Mirror         = 1073741824,

//    private static List<Mods> availableModsStandard = Arrays.asList(
//            new Mods(2048, "Autoplay", "AP"),
//            new Mods(32768, "Key4", ""),
//            new Mods(65536, "Key5", ""),
//            new Mods(131072, "Key6", ""),
//            new Mods(262144, "Key7", ""),
//            new Mods(524288, "Key8", ""),
//            new Mods(1048576, "FadeIn", ""),
//            new Mods(2097152, "Random", ""),
//            new Mods(4194304, "Cinema", ""),
//            new Mods(8388608, "Target", ""),
//            new Mods(16777216, "Key9", ""),
//            new Mods(33554432, "KeyCoop", ""),
//            new Mods(67108864, "Key1", ""),
//            new Mods(134217728, "Key3", ""),
//            new Mods(268435456, "Key2", ""),
//            new Mods(1073741824, "Mirror", ""),
//    )

    public static List<String> parseModsStandard(int mods) {
        List<String> result = new ArrayList<>();

        for(ModsStandard mod : ModsStandard.values()) {
            if((mods & mod.type) > 0 && mod.modInitial.length() > 0) {
                result.add(mod.modInitial);
            }
        }

        return result;
    }

    public enum ModsStandard {
        None(0, "None", ""),
        NoFail(1, "NoFail", "NF"),
        Easy(2, "Easy", "EZ"),
        TouchDevice(4, "TouchDevice", "TD"),
        Hidden(8, "Hidden", "HD"),
        HardRock(16, "HardRock", "HR"),
        SuddenDeath(32, "SuddenDeath", "SD"),
        DoubleTime(64, "DoubleTime", "DT"),
        Relax(128, "Relax", "RX"),
        HalfTime(256, "HalfTime", "HT"),
        Nightcore(512, "Nightcore", "NC"),
        Flashlight(1024, "Flashlight", "FL"),
        SpunOut(4096, "SpunOut", "SO"),
        Relax2(8192, "Relax2", "AP"),
        Perfect(16384, "Perfect", "PF"),
        ScoreV2(536870912, "ScoreV2", "V2"),;

        int type;
        String modName;
        String modInitial;

        private ModsStandard(int type, String modName, String modInitial) {
            this.type = type;
            this.modName = modName;
            this.modInitial = modInitial;
        }

        public int getType() {
            return type;
        }

        public String getModName() {
            return modName;
        }

        public String getModInitial() {
            return modInitial;
        }
    }
}

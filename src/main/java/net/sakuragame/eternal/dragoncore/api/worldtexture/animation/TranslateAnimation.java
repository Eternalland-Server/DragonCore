package net.sakuragame.eternal.dragoncore.api.worldtexture.animation;


public class TranslateAnimation implements Animation {
    public String direction = "x";
    public int delay = 0;
    public float distance = 0;
    public int duration = 0;
    public int cycleCount = 0;
    public boolean fixed = false;
    public int resetTime = 0;
    public boolean bindEntity = false;

    @Override
    public String toData() {
        return "translate#" + direction + "#" + delay + "#" + distance + "#" + duration + "#" + cycleCount + "#" + fixed + "#" + resetTime + "#" + bindEntity;
    }


    @Override
    public String toString() {
        return "TranslateAnimation{" +
                "direction='" + direction + '\'' +
                ", delay=" + delay +
                ", distance=" + distance +
                ", duration=" + duration +
                ", cycleCount=" + cycleCount +
                ", fixed=" + fixed +
                ", resetTime=" + resetTime +
                ", bindEntity=" + bindEntity +
                '}';
    }
}

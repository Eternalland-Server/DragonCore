package net.sakuragame.eternal.dragoncore.api.worldtexture.animation;


public class RotateAnimation implements Animation {
    public String direction = "x";
    public int delay = 0;
    public float angle = 0;
    public int duration = 0;
    public int cycleCount = 0;
    public boolean fixed = false;
    public int resetTime = 0;

    @Override
    public String toData() {
        return "rotate#" + direction + "#" + delay + "#" + angle + "#" + duration + "#" + cycleCount + "#" + fixed + "#" + resetTime;
    }

    @Override
    public String toString() {
        return "RotateAnimation{" +
                "direction='" + direction + '\'' +
                ", delay=" + delay +
                ", angle=" + angle +
                ", duration=" + duration +
                ", cycleCount=" + cycleCount +
                ", fixed=" + fixed +
                ", resetTime=" + resetTime +
                '}';
    }
}

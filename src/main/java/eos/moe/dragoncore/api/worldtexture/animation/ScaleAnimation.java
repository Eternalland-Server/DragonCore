package eos.moe.dragoncore.api.worldtexture.animation;

import lombok.Builder;


public class ScaleAnimation implements Animation {

    public int delay = 0;
    public float fromScale = 0;
    public float toScale = 1;
    public int duration = 0;
    public int cycleCount = 0;
    public boolean fixed = false;
    public int resetTime = 0;

    @Override
    public String toData() {
        return "scale#" + +delay + "#" + fromScale + "#" + toScale + "#" + duration + "#" + cycleCount + "#" + fixed + "#" + resetTime;
    }

    @Override
    public String toString() {
        return "ScaleAnimation{" +
                "delay=" + delay +
                ", fromScale=" + fromScale +
                ", toScale=" + toScale +
                ", duration=" + duration +
                ", cycleCount=" + cycleCount +
                ", fixed=" + fixed +
                ", resetTime=" + resetTime +
                '}';
    }
}

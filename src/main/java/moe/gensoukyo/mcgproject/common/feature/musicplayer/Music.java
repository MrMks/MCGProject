package moe.gensoukyo.mcgproject.common.feature.musicplayer;

import net.minecraft.world.World;

import java.io.IOException;
import java.io.InputStream;

public class Music implements IMusic {
    private final IStream stream;
    private String world;
    private double v,x,y,z;
    private int start;
    public Music(IStream stream, int start, World world, double x, double y, double z){
        this.stream = stream;
        this.v = 0;
        this.world = world.getWorldInfo().getWorldName();
        this.x = x;
        this.y = y;
        this.z = z;
        this.start = start;
    }

    @Override
    public void update(World world, double x, double y, double z) {
        this.world = world.getWorldInfo().getWorldName();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public InputStream openStream() throws IOException {
        return stream.openStream();
    }

    @Override
    public void setMaxVolume(float max) {
        this.v = max;
    }

    @Override
    public float getVolume(World world, double x, double y, double z) {
        float volume = (float) v;
        float distanceSq = (float) getDistanceSq(x, y, z);
        if (!matchWorld(world)) volume = 0;
        if (volume != 0) {
            float n = (volume) * 40;
            float nn = n * n;
            float v;
            if (distanceSq <= nn) {
                v = distanceSq / 4 / nn;
            }
            else if (distanceSq > 16 * nn) {
                v = 1;
            }
            else {
                v = (float) (- distanceSq / 12 / nn + 2 * Math.sqrt(distanceSq) / 3 / n - 1 / 3.0);
            }
            volume *= (1 - v);
        }
        return volume;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void updateStart(int start) {
        this.start = start;
    }

    @Override
    public boolean matchWorld(World world) {
        return world.getWorldInfo().getWorldName().equals(this.world);
    }

    protected double getDistanceSq(double x, double y, double z){
        double disX = x - this.x;
        double disY = y - this.y;
        double disZ = z - this.z;
        return disX * disX + disY * disY + disZ * disZ;
    }
}

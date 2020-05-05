package moe.gensoukyo.mcgproject.common.feature.musicplayer;

import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EmptyMusicManager implements IMusicManager {
    @Override
    public String playNew(UUID uuid, IStream stream, World world, double x, double y, double z) {
        return null;
    }

    @Override
    public String playNew(UUID uuid, IStream stream, World world, double x, double y, double z, int start) {
        return null;
    }

    @Override
    public boolean isExist(@Nullable String hash) {
        return false;
    }

    @Override
    public boolean isPlaying(String hash) {
        return false;
    }

    @Override
    public void changeMaxVolume(String hash, float volume) {

    }

    @Override
    public void updatePosition(String hash, World world, double x, double y, double z) {

    }

    @Override
    public void updateVolume(String hash) {

    }

    @Override
    public void closePlaying(String hash) {

    }

    @Override
    public void closeAll(UUID uuid) {

    }

    @Override
    public void closeAll() {

    }

    @Override
    public int getPosition(String hash) {
        return 0;
    }

    @Override
    public void setPosition(String hash, int pos) {

    }
}

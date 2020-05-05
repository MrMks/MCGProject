package moe.gensoukyo.mcgproject.common.feature.musicplayer;

import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IMusicManager {
    /**
     * equal to playNew(uuid, stream, x, y, z, 0)
     * {@link IMusicManager#playNew(UUID, IStream, World, double, double, double, int)}
     * @return
     */
    String playNew(UUID uuid, IStream stream, World world, double x, double y, double z);

    /**
     * generate a new IMusic
     * @param uuid uuid of the EntityMusicPlayer
     * @param stream the stream to play
     * @param world the world the Entity in
     * @param x posX of entity
     * @param y posY of entity
     * @param z posZ of entity
     * @param start the first frame to play
     * @return a string use to identify the music
     */
    String playNew(UUID uuid, IStream stream, World world, double x, double y, double z, int start);

    boolean isExist(@Nullable String hash);
    boolean isPlaying(String hash);

    void changeMaxVolume(String hash, float volume);
    void updatePosition(String hash, World world, double x, double y, double z);
    void updateVolume(String hash);

    void closePlaying(String hash);
    void closeAll(UUID uuid);
    void closeAll();

    int getPosition(String hash);

    /**
     * notice that this method is prepared for sync-player
     * this method will set the start on server
     * this will do not update the musicPlayer on server
     * @param hash the uuid of the IMusic
     * @param pos the position of IMusic need to be
     */
    void setPosition(String hash, int pos);
}

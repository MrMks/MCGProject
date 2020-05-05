package moe.gensoukyo.mcgproject.cilent.feature.musicPlayer;

import moe.gensoukyo.mcgproject.common.feature.musicplayer.IMusic;
import moe.gensoukyo.mcgproject.common.feature.musicplayer.IStream;
import moe.gensoukyo.mcgproject.common.feature.musicplayer.MusicManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class ClientMusicManager extends MusicManager {
    private final HashMap<String, MusicPlayer> playerMap = new HashMap<>();
    private final MusicThread thread = new MusicThread(new IPlayerCallback() {
        private float volume;
        @Override
        public void onMusicStopped(){
            Minecraft.getMinecraft().addScheduledTask(()->{
                Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MUSIC, volume);
                Minecraft.getMinecraft().gameSettings.saveOptions();
            });
        }

        @Override
        public void onMusicStopped(String hash) {
            playerMap.remove(hash);
        }

        @Override
        public void onMusicStarted(){
            Minecraft.getMinecraft().addScheduledTask(()->{
                volume = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MUSIC);
                Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MUSIC, 0);
                Minecraft.getMinecraft().gameSettings.saveOptions();
            });
        }

        @Override
        public void onMusicStarted(String hash) {
            playerMap.remove(hash);
        }

    });

    @Override
    public boolean isPlaying(String hash) {
        return super.isPlaying(hash) && playerMap.containsKey(hash) && playerMap.get(hash).isPlaying();
    }

    @Override
    public String playNew(UUID uuid, IStream stream, World world, double x, double y, double z, int start) {
        if (entityMap.containsKey(uuid)) playerMap.remove(entityMap.get(uuid)).requestStop();

        String hash = super.playNew(uuid, stream, world, x, y, z, start);
        MusicPlayer player = new MusicPlayer(hash, map.get(hash));
        thread.add(player);
        playerMap.put(hash, player);
        return hash;
    }

    @Override
    public void updateVolume(String hash) {
        IMusic music = map.get(hash);
        MusicPlayer player = playerMap.get(hash);
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
        if (player != null) {
            if (music.matchWorld(playerSP.world)) player.updateVolume(playerSP.world, playerSP.posX, playerSP.posY, playerSP.posZ);
            else closePlaying(hash);
        } else closePlaying(hash);
    }

    @Override
    public void closePlaying(String hash) {
        if (playerMap.containsKey(hash)) playerMap.remove(hash).requestStop();
        super.closePlaying(hash);
    }

    @Override
    public void closeAll(UUID uuid) {
        if (entityMap.containsKey(uuid)) playerMap.remove(entityMap.get(uuid)).requestStop();
        super.closeAll(uuid);
    }

    @Override
    public void closeAll() {
        playerMap.values().forEach(MusicPlayer::requestStop);
        playerMap.clear();
        super.closeAll();
    }

    @Override
    public int getPosition(String hash) {
        if (playerMap.containsKey(hash)) return playerMap.get(hash).getPosition();
        return 0;
    }
}

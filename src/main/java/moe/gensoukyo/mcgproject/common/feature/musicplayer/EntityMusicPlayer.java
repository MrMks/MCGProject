package moe.gensoukyo.mcgproject.common.feature.musicplayer;

import com.google.common.collect.Lists;
import moe.gensoukyo.mcgproject.common.entity.MCGEntity;
import moe.gensoukyo.mcgproject.common.network.MusicPlayerGuiPacket;
import moe.gensoukyo.mcgproject.common.network.NetworkWrapper;
import moe.gensoukyo.mcgproject.core.MCGProject;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOps;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author SQwatermark
 * @author MrMks 改写
 */
@MCGEntity("music_player")
public class EntityMusicPlayer extends EntityMinecart {

    private static final DataParameter<Boolean> IS_PLAYING = EntityDataManager.createKey(EntityMusicPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> URL = EntityDataManager.createKey(EntityMusicPlayer.class, DataSerializers.STRING);
    private static final DataParameter<String> OWNER = EntityDataManager.createKey(EntityMusicPlayer.class, DataSerializers.STRING);
    private static final DataParameter<Float> VOLUME = EntityDataManager.createKey(EntityMusicPlayer.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> IMMERSIVE = EntityDataManager.createKey(EntityMusicPlayer.class, DataSerializers.BOOLEAN);

    private boolean isPlaying = false;
    private String streamURL = "";
    private float volume = 1.0f;
    private String owner = "";
    private boolean immersive = false;

    private String musicCode = null;
    private EntityPlayer user;

    public EntityMusicPlayer(World worldIn) {
        super(worldIn);
    }

    public EntityMusicPlayer(World worldIn, double x, double y, double z, String owner) {
        super(worldIn);
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        dataManager.set(OWNER, owner);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(IS_PLAYING, false);
        dataManager.register(URL, "");
        dataManager.register(OWNER, "");
        dataManager.register(VOLUME, 1.0F);
        dataManager.register(IMMERSIVE, false);
    }

    @Override
    @Nonnull
    public ItemStack getCartItem() {
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public Type getType() {
        return Type.RIDEABLE;
	}

    @Override
    protected boolean canBeRidden(@NotNull Entity entityIn) {
        return false;
    }

    @Nonnull
    public IBlockState getDefaultDisplayTile() {
        return Blocks.JUKEBOX.getDefaultState();
    }

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource damagesource, float i) {
        if (!world.isRemote) {
            Entity source = damagesource.getTrueSource();
            if (!(source instanceof EntityPlayer)) {
                return false;
            }
            EntityPlayer player = (EntityPlayer)source;
            //this.owner = dataManager.get(OWNER);
            if(user == null && checkPermission(player)) {
                this.setDead();
            }
            return true;
        }
        return false;
    }

    @Override
    public void setDead() {
        this.stopStream();
        super.setDead();
        isDead = true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            if (user instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) user;
                if (playerMP.hasDisconnected()) user = null;
            } else user = null;
        } else if (this.ticksExisted % 5 == 0) {
            this.immersive = this.dataManager.get(IMMERSIVE);

            boolean d_isPlaying = dataManager.get(IS_PLAYING);
            if (isPlaying && !d_isPlaying) {
                this.stopStream();
            } else if (!isPlaying && d_isPlaying) {
                this.startStream();
            }
            IMusicManager manager = MCGProject.proxy.getMusicManager();
            if (manager.isPlaying(musicCode)) {
                volume = dataManager.get(VOLUME);
                manager.changeMaxVolume(musicCode, volume);
                manager.updatePosition(musicCode, world, this.posX, this.posY, this.posZ);
                manager.updateVolume(musicCode);
            }
            if (!this.immersive && isPlaying) {
                int random2 = rand.nextInt(24) + 1;
                world.spawnParticle(EnumParticleTypes.NOTE, posX, posY + 1.2D, posZ, random2 / 24.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void applyEntityCollision(@NotNull Entity entityIn) {
        if (this.immersive) return;
        super.applyEntityCollision(entityIn);
    }

    public void receivePacket(String url, boolean playing, float volume, String owner, boolean immersive) {
        this.streamURL = url;
        this.isPlaying = playing;
        this.owner = owner;
        this.volume = volume;
        this.immersive = immersive;
        this.dataManager.set(IS_PLAYING, isPlaying);
        this.dataManager.set(URL, streamURL);
        this.dataManager.set(OWNER, owner);
        this.dataManager.set(VOLUME, volume);
        this.dataManager.set(IMMERSIVE, immersive);
    }

    /**
     * This method should only invoke on client, or this method will do nothing
     */
    private void startStream() {
        if (world.isRemote) {
            if (!this.isPlaying) {
                this.isPlaying = true;
                this.streamURL = dataManager.get(URL);
                this.volume = dataManager.get(VOLUME);
                IMusicManager manager = MCGProject.proxy.getMusicManager();
                manager.closeAll(getUniqueID());
                musicCode = manager.playNew(getUniqueID(), ()->new URL(streamURL).openStream(), world, posX, posY, posZ);
                manager.changeMaxVolume(musicCode, volume);
                manager.updateVolume(musicCode);
            }
        }
    }

    /**
     * This method should only invoke on client, or this method will do nothing
     */
    private void stopStream() {
        if (world.isRemote) {
            if (this.isPlaying) {
                this.isPlaying = false;
                MCGProject.proxy.getMusicManager().closeAll(getUniqueID());
                this.musicCode = null;
                this.streamURL = "";
            }
        }
    }

    @Override
    public boolean processInitialInteract(@NotNull EntityPlayer entityPlayer, @NotNull EnumHand hand) {
        if (world.isRemote) {
            this.owner = dataManager.get(OWNER);
        } else {
            if (user != null || !checkPermission(entityPlayer)) {
                entityPlayer.sendMessage(new TextComponentString("已锁定"));
                return true;
            } else {
                // this should always be true since world.isRemote is false
                if (entityPlayer instanceof EntityPlayerMP) {
                    NetworkWrapper.INSTANCE.sendTo(new MusicPlayerGuiPacket(this), (EntityPlayerMP)entityPlayer);
                }
            }
        }
        return true;
    }

    @Override
    protected void writeEntityToNBT(@NotNull NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setString("StreamUrl", this.streamURL);
        nbttagcompound.setBoolean("isPlaying", this.isPlaying);
        nbttagcompound.setString("owner", this.owner);
        nbttagcompound.setFloat("volume", this.volume);
        nbttagcompound.setBoolean("immersive", this.immersive);
    }

    @Override
    protected void readEntityFromNBT(@NotNull NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.streamURL = nbttagcompound.getString("StreamUrl");
        this.isPlaying = nbttagcompound.getBoolean("isPlaying");
        this.owner = nbttagcompound.getString("owner");
        this.volume = nbttagcompound.getFloat("volume");
        this.immersive = nbttagcompound.getBoolean("immersive");
        this.dataManager.set(URL, streamURL);
        this.dataManager.set(IS_PLAYING, isPlaying);
        this.dataManager.set(OWNER, owner);
        this.dataManager.set(VOLUME, volume);
        this.dataManager.set(IMMERSIVE, immersive);
    }

    public boolean checkPermission(EntityPlayer player) {
        this.owner = dataManager.get(OWNER);
        if (player == null) return false;
        else if (player.getName().equals(this.owner) || this.owner.isEmpty()) return true;
        else if (player.getServer() != null) {
            MinecraftServer server = player.getServer();
            if (server.isDedicatedServer()) {
                UserListOps ops = player.getServer().getPlayerList().getOppedPlayers();
                ArrayList<String> list = Lists.newArrayList(ops.getKeys());
                return list.contains(player.getName());
            } else {
                return player.isCreative();
            }
        }
        return false;
    }

    /**
     *
     * @return dataManager#get(IS_PLAYING), 返回同步的数据
     */
    public boolean isPlaying(){
        return dataManager.get(IS_PLAYING);
    }

    public String getOwner(){
        return dataManager.get(OWNER);
    }

    public float getVolume() {
        return dataManager.get(VOLUME);
    }

    public String getStreamURL() {
        return dataManager.get(URL);
    }

    public boolean isImmersive() {
        return dataManager.get(IMMERSIVE);
    }

    public void onUserExit(EntityPlayer player) {
        if (player == user) user = null;
    }

    //TODO：bgm模式（到达位置播放）和dj模式(同步播放) DISCARD

}
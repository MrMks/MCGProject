package moe.gensoukyo.mcgproject.common.network;

import io.netty.buffer.ByteBuf;
import moe.gensoukyo.mcgproject.common.feature.musicplayer.EntityMusicPlayer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author MrMks
 * 当一个音乐播放器实体被操作时，音乐播放器应不再可被其它玩家操作
 * 为此，在音乐播放器中添加一个user字段来记录当前的操作玩家
 * 为了能够清除该字段的数据，将使用这个包与服务端通信
 */
public class MusicPlayerGuiClosePackage implements IMessage {

    private int entityId;

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static class MusicPlayerGuiCloseHandler implements IMessageHandler<MusicPlayerGuiClosePackage, IMessage> {
        @Override
        public IMessage onMessage(MusicPlayerGuiClosePackage message, MessageContext ctx) {
            Entity entity = ctx.getServerHandler().player.world.getEntityByID(message.entityId);
            if (entity instanceof EntityMusicPlayer) {
                ((EntityMusicPlayer) entity).onUserExit(ctx.getServerHandler().player);
            }
            return null;
        }
    }
}

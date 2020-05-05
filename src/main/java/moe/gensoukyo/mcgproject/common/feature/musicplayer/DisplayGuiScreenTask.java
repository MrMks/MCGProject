package moe.gensoukyo.mcgproject.common.feature.musicplayer;

import moe.gensoukyo.mcgproject.cilent.gui.GuiMusicPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 用于显示GUI，客户端的GuiMusicPlayer类不能直接出现在EntityMusicPlayer类下，所以用了一层套娃
 */
@SideOnly(Side.CLIENT)
public class DisplayGuiScreenTask {
    public DisplayGuiScreenTask(EntityMusicPlayer musicPlayer) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer entityPlayer = mc.player;
        mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiMusicPlayer(entityPlayer, musicPlayer)));
    }
}

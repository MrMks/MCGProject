package moe.gensoukyo.mcgproject.common.creativetab;

import moe.gensoukyo.mcgproject.core.MCGProject;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MCGTabs {

    public static final CreativeTabs TOUHOU = new CreativeTabs(getLabel("touhou"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static final CreativeTabs NORMAL = new CreativeTabs(getLabel("normal"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static final CreativeTabs MODERN = new CreativeTabs(getLabel("modern"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static final CreativeTabs CLASSICAL = new CreativeTabs(getLabel("classical"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static final CreativeTabs FANTASY = new CreativeTabs(getLabel("fantasy"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static final CreativeTabs NATURE = new CreativeTabs(getLabel("nature"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static final CreativeTabs FARM = new CreativeTabs(getLabel("farm"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static final CreativeTabs CLOTHES = new CreativeTabs(getLabel("clothes"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static final CreativeTabs FN = new CreativeTabs(getLabel("fn"))
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.APPLE);
        }
    };

    public static String getLabel(String name) {
        return MCGProject.ID + "." + name;
    }

}

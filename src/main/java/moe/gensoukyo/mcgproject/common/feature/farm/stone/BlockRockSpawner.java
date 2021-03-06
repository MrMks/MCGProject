package moe.gensoukyo.mcgproject.common.feature.farm.stone;

import moe.gensoukyo.mcgproject.common.creativetab.MCGTabs;
import moe.gensoukyo.mcgproject.common.entity.EntityItemMCG;
import moe.gensoukyo.mcgproject.core.MCGProject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.Random;

public class BlockRockSpawner extends Block {

    public BlockRockSpawner() {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setCreativeTab(MCGTabs.NATURE);
        this.setRegistryName("rock_spawner");
        this.setTranslationKey(MCGProject.ID + "." + "rock_spawner");
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0)) {
            List<Entity> entities = worldIn.getEntitiesWithinAABB(EntityItemMCG.class, new AxisAlignedBB(pos.up(15).east(15).north(15), pos.down(15).west(15).south(15)));
            if(entities.size() > 4) return;
            int x = pos.getX() + rand.nextInt(20) - 10;
            int z = pos.getZ() + rand.nextInt(20) - 10;
            int y = worldIn.getHeight(x, z);
            EntityItemMCG stone;
            MCGProject.logger.info(canSpawn(worldIn, x, y - 1, z));
            if (canSpawn(worldIn, x, y - 1, z)) {
                stone = new EntityItemMCG(worldIn, x, y, z, new ItemStack(Blocks.GRAVEL));
                worldIn.spawnEntity(stone);
            }
            ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
    }

    private boolean canSpawn(World world, int x, int y, int z) {
        Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        return block == Blocks.STONE || block == Blocks.GRAVEL || block == Blocks.GRASS || block == Blocks.DIRT;
    }

}

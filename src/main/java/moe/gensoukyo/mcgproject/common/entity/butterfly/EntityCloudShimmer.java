package moe.gensoukyo.mcgproject.common.entity.butterfly;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityCloudShimmer extends EntityBasicButterfly
{

    public EntityCloudShimmer(World worldIn)
    {
        super(worldIn);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        this.setVariant(ButterflyType.CLOUDSHIMMER.getMetadata());
        return super.onInitialSpawn(difficulty, livingdata);
    }
}
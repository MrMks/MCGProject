package moe.gensoukyo.mcgproject.common.feature;

import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 阻止鸡蛋生成鸡，阻止农田被踩坏
 */
public class WorldGuard {

    private static WorldGuard instance;
    public static WorldGuard instance() {
        if(instance == null) instance = new WorldGuard();
        return instance;
    }

    @SubscribeEvent
    public void dontSpawnChicken(ProjectileImpactEvent.Throwable event) {
        EntityThrowable throwable = event.getThrowable();
        if (throwable instanceof EntityEgg) {
            event.setCanceled(true);
            if (event.getRayTraceResult().entityHit != null) {
                event.getRayTraceResult().entityHit.attackEntityFrom(DamageSource.causeThrownDamage(throwable, throwable.getThrower()), 0.0F);
            }
            if (!throwable.world.isRemote) {
                throwable.world.setEntityState(throwable, (byte)3);
                throwable.setDead();
            }
        }
    }

    @SubscribeEvent
    public void dontTrampleFarmland(BlockEvent.FarmlandTrampleEvent event) {
        event.setCanceled(true);
    }

}
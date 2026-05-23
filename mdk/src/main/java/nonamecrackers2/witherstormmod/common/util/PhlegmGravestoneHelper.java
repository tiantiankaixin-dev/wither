package nonamecrackers2.witherstormmod.common.util;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.mixin.MixinLivingEntityAccessor;

public class PhlegmGravestoneHelper {
   public static Optional<Vec3> findPotentialPhlegmClusterPos(LivingEntity entity, DamageSource damageSource) {
      if ((Boolean)WitherStormModConfig.SERVER.preserveDropsForAllMobs.get() || entity instanceof Player) {
         ItemPreservationCondition method = (ItemPreservationCondition)WitherStormModConfig.SERVER.itemPreservation.get();
         if ((method.useDirectEntity() ? damageSource.getDirectEntity() : damageSource.getEntity()) instanceof WitherStormEntity storm && storm.isAlive()) {
            return Optional.ofNullable(method.getPos(storm, entity));
         }
      }

      return Optional.empty();
   }

   public static void spawnForEntity(LivingEntity entity, Vec3 pos, List<ItemStack> items) {
      int reward = ForgeEventFactory.getExperienceDrop(entity, ((MixinLivingEntityAccessor)entity).witherstormmod$getLastHurtByPlayer(), entity.getExperienceReward());
      entity.skipDropExperience();
      BlockClusterEntity cluster = ClusterBuilderHelper.buildPhlegmClusterWithItems(entity.level(), entity.getRandom(), items, entity.getDisplayName(), reward);
      if (cluster.getSize() > 0) {
         cluster.setPos(pos);
         cluster.setDeltaMovement(entity.getRandom().nextGaussian() * 0.3, 0.0, entity.getRandom().nextGaussian() * 0.3);
         cluster.setRotationDelta(new Vec2((float)entity.getRandom().nextInt(20) * 0.3F / 2.0F, (float)entity.getRandom().nextInt(20) * 0.3F / 2.0F));
         entity.level().addFreshEntity(cluster);
         cluster.setSink(1);
         cluster.setAntiStacking(true);
      }
   }
}

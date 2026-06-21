package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;

public final class WitherStormModMobTypes {
   private WitherStormModMobTypes() {
   }

   public static boolean isSickened(Entity entity) {
      return entity instanceof WitherSickened
         || entity instanceof WitherStormEntity
         || entity instanceof WitherStormHeadEntity
         || entity instanceof TentacleEntity
         || entity instanceof WitheredSymbiontEntity
         || entity.getType().is(WitherStormModEntityTags.SICKENED_MOBS);
   }

   public static boolean isIllager(LivingEntity entity) {
      return entity.getType().is(EntityTypeTags.ILLAGER);
   }
}

package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class EvokerFangsSpell extends SymbiontSpell {
   private static final SimpleWeightedRandomList<EntityType<? extends Mob>> PILLAGERS = SimpleWeightedRandomList.<EntityType<? extends Mob>>builder()
      .add(WitherStormModEntityTypes.SICKENED_PILLAGER.get(), 3)
      .add(WitherStormModEntityTypes.SICKENED_VINDICATOR.get(), 3)
      .build();

   public EvokerFangsSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void cast(@Nonnull LivingEntity target) {
      int chance = this.entity.shouldIncreaseDifficulty() ? 6 : 3;
      if (this.entity.getRandom().nextInt(chance) == 1) {
         for (int i = 0; i < 3; i++) {
            Mob mob = WorldUtil.summonRandomMob(
               (ServerLevel)this.entity.level(), this.entity.blockPosition(), this.entity.getRandom(), 6, PILLAGERS, this.entity.shouldIncreaseDifficulty()
            );
            if (mob != null) {
               addAttributes(mob);
            }
         }
      }

      LivingEntity entity = this.entity.getRandomNearbyTargetOrFallback(target, WitheredSymbiontEntity.TARGET_PREDICATE);
      double minHeight = Math.min(entity.getY(), this.entity.getY()) - 2.0;
      double maxHeight = Math.max(entity.getY(), this.entity.getY()) + 2.0;
      int ringCount = 16;
      int fangsPerRing = 8;
      double initialRadius = 1.5;
      double expansionRate = 3.0 + this.entity.getRandom().nextDouble();

      for (int ix = 0; ix < ringCount; ix++) {
         double radius = initialRadius + (double)ix * expansionRate;
         fangsPerRing += 4;
         double angleIncrement = (Math.PI * 2) / (double)fangsPerRing;

         for (int i1 = 0; i1 < fangsPerRing; i1++) {
            double angle = (double)i1 * angleIncrement;
            double x = this.entity.getX() + Math.cos(angle) * radius;
            double z = this.entity.getZ() + Math.sin(angle) * radius;
            this.createFang(x, z, minHeight, maxHeight, (float)angle, i1 + 2);
         }
      }
   }

   private void createFang(double x, double z, double minHeight, double maxHeight, float yRot, int delay) {
      BlockPos blockPos = BlockPos.containing(x, maxHeight, z);
      boolean flag = false;
      double d0 = 0.0;

      do {
         BlockPos below = blockPos.below();
         BlockState state = this.entity.level().getBlockState(below);
         if (state.isFaceSturdy(this.entity.level(), below, Direction.UP)) {
            if (!this.entity.level().isEmptyBlock(blockPos)) {
               BlockState state1 = this.entity.level().getBlockState(blockPos);
               VoxelShape shape = state1.getCollisionShape(this.entity.level(), blockPos);
               if (!shape.isEmpty()) {
                  d0 = shape.max(Axis.Y);
               }
            }

            flag = true;
            break;
         }

         blockPos = blockPos.below();
      } while (blockPos.getY() >= Mth.floor(minHeight) - 1);

      if (flag) {
         this.entity.level().addFreshEntity(new EvokerFangs(this.entity.level(), x, (double)blockPos.getY() + d0, z, yRot, delay, this.entity));
      }
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(60, random.nextInt(100)) - Mth.floor(modifier) * 10;
   }

   private static void addAttributes(Mob mob) {
      Objects.requireNonNull(mob.getAttribute(Attributes.MAX_HEALTH))
         .addPermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath("witherstormmod", "symbiont_summon_health"), -((mob.getRandom().nextDouble() + 0.5) * 2.0), Operation.ADD_VALUE));
      Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED))
         .addPermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath("witherstormmod", "symbiont_summon_speed"), -0.08, Operation.ADD_VALUE));
   }
}

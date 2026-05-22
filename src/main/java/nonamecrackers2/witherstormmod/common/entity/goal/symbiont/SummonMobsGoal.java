package nonamecrackers2.witherstormmod.common.entity.goal.symbiont;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class SummonMobsGoal extends Goal {
   protected final WitheredSymbiontEntity entity;
   protected final SimpleWeightedRandomList<EntityType<? extends Mob>> spawnTypes;
   protected final SimpleWeightedRandomList<EntityType<? extends Mob>> difficultSpawnTypes;
   protected int time;

   public SummonMobsGoal(
      WitheredSymbiontEntity entity,
      SimpleWeightedRandomList<EntityType<? extends Mob>> types,
      SimpleWeightedRandomList<EntityType<? extends Mob>> difficultTypes
   ) {
      this.entity = entity;
      this.spawnTypes = types;
      this.difficultSpawnTypes = difficultTypes;
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET, Flag.LOOK));
   }

   public boolean canUse() {
      LivingEntity target = this.entity.getTarget();
      return target != null && target.isAlive() ? !this.spawnTypes.isEmpty() : false;
   }

   public boolean canContinueToUse() {
      return this.canUse() && this.time > 0;
   }

   public void start() {
      this.time = 60 + this.entity.getRandom().nextInt(60) + (this.entity.shouldIncreaseDifficulty() ? 40 : 0);
      this.entity.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_SUMMON.get(), 4.0F, 1.0F);
   }

   public void tick() {
      if (this.time > 0) {
         this.time--;
         if (this.time % 10 == 0) {
            Mob mob = WorldUtil.summonRandomMob(
               (ServerLevel)this.entity.level(),
               this.entity.blockPosition(),
               this.entity.getRandom(),
               16,
               this.entity.shouldIncreaseDifficulty() ? this.difficultSpawnTypes : this.spawnTypes,
               this.entity.shouldIncreaseDifficulty()
            );
            if (mob != null) {
               addAttributes(mob);
            }
         }
      }
   }

   public void stop() {
      this.entity.nextStage();
   }

   private static void addAttributes(Mob mob) {
      Objects.requireNonNull(mob.getAttribute(Attributes.MAX_HEALTH))
         .addPermanentModifier(new AttributeModifier("194fec31-b36e-41fc-ad72-02a5cb891def", -((mob.getRandom().nextDouble() + 0.5) * 2.0), Operation.ADDITION));
      Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED))
         .addPermanentModifier(new AttributeModifier("5965c24d-8ac1-4f04-92ee-3d2724f976e8", -0.08, Operation.ADDITION));
   }
}

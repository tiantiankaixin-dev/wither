package nonamecrackers2.witherstormmod.common.entity.goal.symbiont;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.registry.WitherStormModRegistries;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSymbiontSpellTypes;

public class PrepareSpellGoal extends Goal {
   protected final WitheredSymbiontEntity entity;

   public PrepareSpellGoal(WitheredSymbiontEntity entity) {
      this.entity = entity;
   }

   public boolean canUse() {
      LivingEntity target = this.entity.getTarget();
      if (target == null || !target.isAlive()) {
         return false;
      } else {
         return this.entity.isCastingSpell() ? false : this.entity.canPickSpell();
      }
   }

   public boolean canContinueToUse() {
      LivingEntity target = this.entity.getTarget();
      return target != null && target.isAlive() && this.entity.canPickSpell();
   }

   public void start() {
      this.entity.setSpell(getRandomSpell(this.entity.getRandom(), this.entity.getSpell()));
      this.entity.getUseSpellGoal().nextAttackTickCount = this.entity.tickCount + 40 + this.entity.getRandom().nextInt(20);
      this.entity.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_PREPARE_SPELL.get(), 4.0F, 1.0F);
      this.entity.setNextSpellPickCount(400 + this.entity.getRandom().nextInt(400) - (this.entity.shouldIncreaseDifficulty() ? 320 : 0));
      if (this.entity.shouldNotGoOverHalfHealth() && this.entity.getHealth() / this.entity.getMaxHealth() <= 0.5F) {
         this.entity.setHalfHealthLimit(false);
      }
   }

   public static SpellType getRandomSpell(RandomSource random, SpellType toRemove) {
      return (SpellType)Util.getRandom(
         WitherStormModRegistries.SPELL_TYPES
            .get()
            .getValues()
            .stream()
            .filter(s -> s != WitherStormModSymbiontSpellTypes.EMPTY.get() && s != toRemove)
            .toList(),
         random
      );
   }
}

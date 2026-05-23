package nonamecrackers2.witherstormmod.common.entity.goal.symbiont;

import java.util.EnumSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.player.Player;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class UseSpellGoal extends Goal {
   protected final WitheredSymbiontEntity entity;
   public int nextAttackTickCount;

   public UseSpellGoal(WitheredSymbiontEntity entity) {
      this.entity = entity;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public boolean canUse() {
      LivingEntity target = this.entity.getTarget();
      if (target == null || !target.isAlive()) {
         return false;
      } else if (this.entity.isCastingSpell()) {
         return false;
      } else if (!this.entity.hasSpell()) {
         return false;
      } else {
         return this.entity.getSpellInstance() == null ? false : this.entity.tickCount > this.nextAttackTickCount;
      }
   }

   public boolean canContinueToUse() {
      LivingEntity target = this.entity.getTarget();
      return target != null && target.isAlive() && this.entity.hasSpell() && this.entity.tickCount >= this.nextAttackTickCount || this.entity.isCastingSpell();
   }

   public void start() {
      float effectiveDifficulty = this.entity.level().getCurrentDifficultyAt(this.entity.blockPosition()).getEffectiveDifficulty();
      int spellDelay = this.entity
         .getSpellInstance()
         .getDelay(this.entity.getRandom(), effectiveDifficulty + (float)(this.entity.shouldIncreaseDifficulty() ? 60 : 0));
      if (spellDelay < this.entity.getSpell().spellTime() + 10) {
         spellDelay = this.entity.getSpell().spellTime() + 10;
      }

      this.nextAttackTickCount = this.entity.tickCount + spellDelay;
      this.entity.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_CAST_SPELL.get(), 4.0F, 1.0F);
      this.entity.beginSpellCasting();
      if (this.entity.getTarget() instanceof Player) {
         this.entity.spellUsed();
      }
   }
}

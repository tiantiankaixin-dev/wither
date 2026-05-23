package nonamecrackers2.witherstormmod.common.entity;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class TaintedSlime extends Slime {
   public AnimationState idle = new AnimationState();

   public TaintedSlime(EntityType<? extends TaintedSlime> type, Level level) {
      super(type, level);
      this.idle.start(this.tickCount);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes();
   }
}

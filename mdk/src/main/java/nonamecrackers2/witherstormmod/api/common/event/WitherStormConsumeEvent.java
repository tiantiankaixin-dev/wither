package nonamecrackers2.witherstormmod.api.common.event;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
// TODO_MIG[CANCELABLE]: removed; class must implement ICancellableEvent
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

// TODO_MIG[CANCELABLE]: implement ICancellableEvent on this class instead
public class WitherStormConsumeEvent extends WitherStormEvent {
   @Nullable
   private final Entity consumedEntity;
   private int consumedAmount;

   public WitherStormConsumeEvent(WitherStormEntity storm, @Nullable Entity consumedEntity, int consumedAmount) {
      super(storm);
      this.consumedEntity = consumedEntity;
      this.consumedAmount = consumedAmount;
   }

   @Nullable
   public Entity getConsumedEntity() {
      return this.consumedEntity;
   }

   public int getConsumedAmount() {
      return this.consumedAmount;
   }

   public void setConsumedAmount(int amount) {
      this.consumedAmount = amount;
   }
}

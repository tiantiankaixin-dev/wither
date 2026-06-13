package nonamecrackers2.witherstormmod.api.common.event;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.ICancellableEvent;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormConsumeEvent extends WitherStormEvent implements ICancellableEvent {
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

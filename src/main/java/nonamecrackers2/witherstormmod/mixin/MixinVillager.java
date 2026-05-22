package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import nonamecrackers2.witherstormmod.common.util.BrainInjectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Villager.class})
public abstract class MixinVillager {
   @Inject(
      method = {"registerBrainGoals"},
      at = {@At("TAIL")}
   )
   public void registerBrainGoalsTail(Brain<Villager> brain, CallbackInfo ci) {
      BrainInjectionHelper.inject((Villager)(Object)this);
   }
}

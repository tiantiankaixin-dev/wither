package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SplashManager.class})
public class MixinSplashManager {
   @Shadow
   private List<String> splashes;

   @Inject(
      method = {"apply"},
      at = {@At("TAIL")}
   )
   public void witherstormmod$addCustomSplashes$apply(List<String> splashes, ResourceManager manager, ProfilerFiller filler, CallbackInfo ci) {
      this.splashes.add("Waga Baga Bobo!");
      this.splashes.add("Also try Explorer's Eve!");
      this.splashes.add("Also try CWSM Plus!");
      this.splashes.add("Also try Command Tool Expansion!");
      this.splashes.add("Also try Bloxxify's Lightbringer!");
      this.splashes.add("It's symbiont, not 'symbiote'!");
      this.splashes.add("Useless tentacles included!");
      this.splashes.add("Elixir not included!");
      this.splashes.add("Slightly optimized Wither Storm included!");
      this.splashes.add("Shout out to Nazaru!");
      this.splashes.add("World destruction included!");
      this.splashes.add("Also try Story Mod!");
      this.splashes.add("Includes zombies doing flips!");
      this.splashes.add("Rest in peace Sickened Frog and Rabbit!");
      this.splashes.add("IT SHOULD HAVE BEEN ME!!");
   }
}

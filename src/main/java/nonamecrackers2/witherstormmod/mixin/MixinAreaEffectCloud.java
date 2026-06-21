package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({AreaEffectCloud.class})
public interface MixinAreaEffectCloud {
   @Accessor("potionContents")
   PotionContents witherstormmod$getPotionContents();

   @Accessor("potionContents")
   void witherstormmod$setPotionContents(PotionContents contents);
}

package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import javax.annotation.Nonnull;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class EmptySpell extends SymbiontSpell {
   public EmptySpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void cast(@Nonnull LivingEntity target) {
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return 0;
   }
}

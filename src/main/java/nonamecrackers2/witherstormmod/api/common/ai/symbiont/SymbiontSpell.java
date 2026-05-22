package nonamecrackers2.witherstormmod.api.common.ai.symbiont;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public abstract class SymbiontSpell {
   protected final WitheredSymbiontEntity entity;
   protected final SpellType type;
   protected final List<Entity> projectiles = Lists.newArrayList();

   public SymbiontSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      this.entity = symbiont;
      this.type = type;
   }

   public void start(@Nonnull LivingEntity target) {
   }

   public abstract void cast(@Nonnull LivingEntity var1);

   public void finish() {
      for (Entity projectile : this.projectiles) {
         projectile.setNoGravity(false);
      }

      this.projectiles.clear();
   }

   public void doCasting(@Nonnull LivingEntity target) {
   }

   public abstract int getDelay(RandomSource var1, float var2);
}

package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;

public class EntityConversionEvents {
   @SubscribeEvent
   public static void onLivingDeath(LivingDeathEvent event) {
      Level world = event.getEntity().level();
      if (!world.isClientSide && (Boolean)WitherStormModConfig.SERVER.sickenedMobConversions.get()) {
         DamageSource source = event.getSource();
         if (source.is(WitherStormModDamageTypes.WITHER_SICKNESS)
            && event.getEntity() instanceof Mob mob
            && WorldTainting.getInstance().convertMob(mob, true)) {
            event.setCanceled(true);
         }
      }
   }
}

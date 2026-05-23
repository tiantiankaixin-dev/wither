package nonamecrackers2.witherstormmod.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.ClientTickEvent
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.Phase
import nonamecrackers2.witherstormmod.client.particle.CommandBlockParticle;
import nonamecrackers2.witherstormmod.client.particle.PhlegmBlockParticle;
import nonamecrackers2.witherstormmod.client.particle.TractorBeamParticle;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModItemTags;

public class ParticleEvents {
   public static void registerFactories(RegisterParticleProvidersEvent event) {
      event.registerSpriteSet((ParticleType)WitherStormModParticleTypes.COMMAND_BLOCK.get(), CommandBlockParticle.Factory::new);
      event.registerSpriteSet((ParticleType)WitherStormModParticleTypes.TRACTOR_BEAM.get(), TractorBeamParticle.Factory::new);
      event.registerSpriteSet((ParticleType)WitherStormModParticleTypes.PHLEGM.get(), PhlegmBlockParticle.Factory::new);
   }

   public static void onClientTick(ClientTickEvent event) {
      Minecraft mc = Minecraft.getInstance();
      if (event.phase == Phase.START && !mc.isPaused() && mc.level != null) {
         ClientLevel world = mc.level;

         for (Entity entity : world.entitiesForRendering()) {
            if (entity instanceof ItemEntity) {
               ItemEntity item = (ItemEntity)entity;
               if (item.getItem().is((Item)WitherStormModItems.COMMAND_BLOCK_BOOK.get())
                  || item.getItem().is(WitherStormModItemTags.COMMAND_BLOCK_TOOLS)) {
                  for (int i = 0; i < 2; i++) {
                     double x = item.getX() + world.getRandom().nextGaussian() * 0.4;
                     double y = item.getEyeY() + world.getRandom().nextGaussian() * 0.4;
                     double z = item.getZ() + world.getRandom().nextGaussian() * 0.4;
                     Vec3 delta = item.getEyePosition(1.0F).subtract(x, y, z).normalize().multiply(0.05, 0.05, 0.05);
                     world.addParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, delta.x(), delta.y(), delta.z());
                  }
               }
            }
         }
      }
   }
}

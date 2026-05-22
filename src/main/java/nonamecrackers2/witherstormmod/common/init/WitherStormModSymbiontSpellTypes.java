package nonamecrackers2.witherstormmod.common.init;

import java.util.Optional;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.registry.WitherStormModRegistries;
import nonamecrackers2.witherstormmod.common.entity.FlamingWitherSkullEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.ArrowsSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.BombingSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.EmptySpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.EvokerFangsSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.FireballSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.PullSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.PulseSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.ShulkerBulletsSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.SmashSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.ThrowingSpell;
import nonamecrackers2.witherstormmod.common.entity.ai.symbiont.WitherSkullsSpell;
import nonamecrackers2.witherstormmod.common.util.DragonFireballAccessor;

public class WitherStormModSymbiontSpellTypes {
   private static final DeferredRegister<SpellType> SPELL_TYPES = DeferredRegister.create(WitherStormModRegistries.SPELL_TYPES_NAME, "witherstormmod");
   public static final RegistryObject<SpellType> EMPTY = SPELL_TYPES.register("empty", () -> new SpellType(EmptySpell::new, 0, Optional.empty(), false));
   public static final RegistryObject<SpellType> EVOKER_FANGS = SPELL_TYPES.register(
      "evoker_fangs", () -> new SpellType(EvokerFangsSpell::new, 20, Optional.empty())
   );
   public static final RegistryObject<SpellType> SHULKER_BULLETS = SPELL_TYPES.register(
      "shulker_bullets", () -> new SpellType(ShulkerBulletsSpell::new, 80, Optional.empty())
   );
   public static final RegistryObject<SpellType> ARROWS = SPELL_TYPES.register("arrows", () -> new SpellType(ArrowsSpell::new, 120, Optional.empty()));
   public static final RegistryObject<SpellType> SMASH = SPELL_TYPES.register("smash", () -> new SpellType(SmashSpell::new, 40, Optional.empty()));
   public static final RegistryObject<SpellType> FIRE_BALLS = SPELL_TYPES.register("fire_balls", () -> new SpellType((s, t) -> {
         int randomFireball = s.getRandom().nextInt(7);

         FireballSpell.ProjectileFactory factory = switch (randomFireball) {
            case 0 -> SmallFireball::new;
            case 1 -> (level, entity, x, y, z) -> {
            DragonFireball fireball = new DragonFireball(level, entity, x, y, z);
            ((DragonFireballAccessor)fireball).setCreatedBySymbiont(true);
            return fireball;
         };
            case 2 -> FlamingWitherSkullEntity::new;
            default -> (level, entity, x, y, z) -> new LargeFireball(level, entity, x, y, z, 1);
         };

         int amount = switch (randomFireball) {
            case 0 -> 16;
            case 1 -> 6;
            case 2 -> 4;
            default -> 8;
         };
         return new FireballSpell(s, t, factory, amount);
      }, 160, Optional.empty()));
   public static final RegistryObject<SpellType> WITHER_SKULLS = SPELL_TYPES.register(
      "wither_skulls", () -> new SpellType(WitherSkullsSpell::new, 220, Optional.empty())
   );
   public static final RegistryObject<SpellType> PULL = SPELL_TYPES.register(
      "pull", () -> new SpellType(PullSpell::new, 240, Optional.of(WitherStormModSoundEvents.WITHERED_SYMBIONT_PULL), false)
   );
   public static final RegistryObject<SpellType> THROWING = SPELL_TYPES.register("throwing", () -> new SpellType(ThrowingSpell::new, 240, Optional.empty()));
   public static final RegistryObject<SpellType> BOMBING = SPELL_TYPES.register("bombing", () -> new SpellType(BombingSpell::new, 60, Optional.empty()));
   public static final RegistryObject<SpellType> PULSE = SPELL_TYPES.register(
      "pulse", () -> new SpellType(PulseSpell::new, 160, Optional.empty(), true, 6.0, 2.0)
   );

   public static void register(IEventBus modBus) {
      SPELL_TYPES.register(modBus);
   }
}

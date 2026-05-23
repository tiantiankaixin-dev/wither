package nonamecrackers2.witherstormmod.common.init;

import javax.annotation.Nullable;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WitherStormModDamageTypes {
   public static final ResourceKey<DamageType> FLAMING_WITHER_SKULL = create("flaming_wither_skull");
   public static final ResourceKey<DamageType> WITHER_SICKNESS = create("wither_sickness");
   public static final ResourceKey<DamageType> PLAYER_FORMIDIBOMB = create("player_formidibomb");
   public static final ResourceKey<DamageType> FORMIDIBOMB = create("formidibomb");
   public static final ResourceKey<DamageType> WITHER_STORM_ATTACK_MOB = create("wither_storm_attack_mob");
   public static final ResourceKey<DamageType> WITHER_STORM_ATTACK = create("wither_storm_attack");
   public static final ResourceKey<DamageType> PLAYER_ATTACK_WITHER_STORM = create("player_attack_wither_storm");
   public static final ResourceKey<DamageType> MOB_ATTACK_WITHER_STORM = create("mob_attack_wither_storm");
   public static final ResourceKey<DamageType> SUPER_TNT_EXPLOSION = create("super_tnt_explosion");
   public static final ResourceKey<DamageType> IRON_PIERCING = create("iron_pierce");

   private static ResourceKey<DamageType> create(String id) {
      return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("witherstormmod", id));
   }

   public static DamageSource source(RegistryAccess access, ResourceKey<DamageType> key) {
      return new DamageSource(access.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
   }

   public static DamageSource source(RegistryAccess access, ResourceKey<DamageType> key, @Nullable Entity entity) {
      return new DamageSource(access.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), entity);
   }

   public static DamageSource source(RegistryAccess access, ResourceKey<DamageType> key, @Nullable Entity entity, @Nullable Entity entity2) {
      return new DamageSource(access.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), entity, entity2);
   }

   public static DamageSource witherStormAttack(LivingEntity entity) {
      return source(entity.level().registryAccess(), WITHER_STORM_ATTACK, entity);
   }

   public static DamageSource witherStormAttackMob(LivingEntity entity) {
      return source(entity.level().registryAccess(), WITHER_STORM_ATTACK_MOB, entity);
   }

   public static DamageSource playerAttackWitherStorm(Player entity) {
      return source(entity.level().registryAccess(), PLAYER_ATTACK_WITHER_STORM, entity);
   }

   public static DamageSource mobAttackWitherStorm(LivingEntity entity) {
      return source(entity.level().registryAccess(), MOB_ATTACK_WITHER_STORM, entity);
   }

   public static DamageSource superTntExplosion(Level level) {
      return source(level.registryAccess(), SUPER_TNT_EXPLOSION);
   }

   public static DamageSource ironPierced(LivingEntity entity) {
      return source(entity.level().registryAccess(), IRON_PIERCING, entity);
   }
}

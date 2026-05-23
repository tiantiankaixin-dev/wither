package nonamecrackers2.witherstormmod.common.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import nonamecrackers2.witherstormmod.api.common.data.MobConversionProvider;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;

public class WitherStormModMobConversionsProvider extends MobConversionProvider {
   public WitherStormModMobConversionsProvider(PackOutput output) {
      super(output, "witherstormmod");
   }

   @Override
   protected void addConversions() {
      this.add(EntityType.ZOMBIE, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_ZOMBIE.get());
      this.add(EntityType.SKELETON, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_SKELETON.get());
      this.add(EntityType.SPIDER, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_SPIDER.get());
      this.add(EntityType.CREEPER, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_CREEPER.get());
      this.add(EntityType.VILLAGER, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_VILLAGER.get());
      this.add(EntityType.ZOMBIE_VILLAGER, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_VILLAGER.get());
      this.add(EntityType.PHANTOM, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_PHANTOM.get());
      this.add(EntityType.CHICKEN, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_CHICKEN.get());
      this.add(EntityType.PARROT, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_PARROT.get());
      this.add(EntityType.COW, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_COW.get());
      this.add(EntityType.PIG, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_PIG.get());
      this.add(EntityType.MOOSHROOM, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get());
      this.add(EntityType.BEE, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_BEE.get());
      this.add(EntityType.PILLAGER, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_PILLAGER.get());
      this.add(EntityType.VINDICATOR, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_VINDICATOR.get());
      this.add(EntityType.STRAY, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_SKELETON.get());
      this.add(EntityType.HUSK, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_ZOMBIE.get());
      this.add(EntityType.DROWNED, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_ZOMBIE.get());
      this.add(EntityType.IRON_GOLEM, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_IRON_GOLEM.get(), false);
      this.add(EntityType.SNOW_GOLEM, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_SNOW_GOLEM.get(), false);
      this.add(EntityType.WOLF, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_WOLF.get());
      this.add(EntityType.CAT, (EntityType<? extends Mob>)WitherStormModEntityTypes.SICKENED_CAT.get());
   }
}

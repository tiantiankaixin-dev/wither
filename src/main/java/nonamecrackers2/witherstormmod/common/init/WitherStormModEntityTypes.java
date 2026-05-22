package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.client.util.ClientBlockClusterFactory;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.BlueFlamingWitherSkullEntity;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.FlamingWitherSkullEntity;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.SickenedBee;
import nonamecrackers2.witherstormmod.common.entity.SickenedCat;
import nonamecrackers2.witherstormmod.common.entity.SickenedChicken;
import nonamecrackers2.witherstormmod.common.entity.SickenedCow;
import nonamecrackers2.witherstormmod.common.entity.SickenedCreeper;
import nonamecrackers2.witherstormmod.common.entity.SickenedIronGolem;
import nonamecrackers2.witherstormmod.common.entity.SickenedMushroomCow;
import nonamecrackers2.witherstormmod.common.entity.SickenedParrot;
import nonamecrackers2.witherstormmod.common.entity.SickenedPhantom;
import nonamecrackers2.witherstormmod.common.entity.SickenedPig;
import nonamecrackers2.witherstormmod.common.entity.SickenedPillager;
import nonamecrackers2.witherstormmod.common.entity.SickenedSkeleton;
import nonamecrackers2.witherstormmod.common.entity.SickenedSnowGolem;
import nonamecrackers2.witherstormmod.common.entity.SickenedSpider;
import nonamecrackers2.witherstormmod.common.entity.SickenedVillager;
import nonamecrackers2.witherstormmod.common.entity.SickenedVindicator;
import nonamecrackers2.witherstormmod.common.entity.SickenedWolf;
import nonamecrackers2.witherstormmod.common.entity.SickenedZombie;
import nonamecrackers2.witherstormmod.common.entity.SuperTNTEntity;
import nonamecrackers2.witherstormmod.common.entity.TaintedSlime;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.TentacleSpike;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class WitherStormModEntityTypes {
   public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "witherstormmod");
   public static final RegistryObject<EntityType<WitherStormEntity>> WITHER_STORM = register(
      "wither_storm", Builder.of(WitherStormEntity::new, MobCategory.MONSTER).sized(0.9F, 3.5F).setTrackingRange(512).clientTrackingRange(512).fireImmune()
   );
   public static final RegistryObject<EntityType<BlockClusterEntity>> BLOCK_CLUSTER = register(
      "block_cluster",
      Builder.of(BlockClusterEntity::new, MobCategory.MISC)
         .sized(1.0F, 1.0F)
         .setTrackingRange(512)
         .clientTrackingRange(512)
         .fireImmune()
         .updateInterval(10)
         .setCustomClientFactory(ClientBlockClusterFactory::make)
   );
   public static final RegistryObject<EntityType<WitherStormSegmentEntity>> WITHER_STORM_SEGMENT = register(
      "wither_storm_segment",
      Builder.<WitherStormSegmentEntity>of(WitherStormSegmentEntity::new, MobCategory.MONSTER).sized(30.0F, 25.0F).setTrackingRange(512).clientTrackingRange(512).fireImmune()
   );
   public static final RegistryObject<EntityType<FlamingWitherSkullEntity>> FLAMING_WITHER_SKULL = register(
      "flaming_wither_skull", Builder.<FlamingWitherSkullEntity>of(FlamingWitherSkullEntity::new, MobCategory.MISC).sized(0.8F, 0.8F).clientTrackingRange(4).updateInterval(10)
   );
   public static final RegistryObject<EntityType<BlueFlamingWitherSkullEntity>> BLUE_FLAMING_WITHER_SKULL = register(
      "blue_flaming_wither_skull", Builder.<BlueFlamingWitherSkullEntity>of(BlueFlamingWitherSkullEntity::new, MobCategory.MISC).sized(0.8F, 0.8F).clientTrackingRange(4).updateInterval(10)
   );
   public static final RegistryObject<EntityType<SickenedZombie>> SICKENED_ZOMBIE = register(
      "sickened_zombie", Builder.of(SickenedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedSkeleton>> SICKENED_SKELETON = register(
      "sickened_skeleton", Builder.of(SickenedSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedSpider>> SICKENED_SPIDER = register(
      "sickened_spider", Builder.of(SickenedSpider::new, MobCategory.MONSTER).sized(1.6F, 1.1F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedCreeper>> SICKENED_CREEPER = register(
      "sickened_creeper", Builder.of(SickenedCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SuperTNTEntity>> SUPER_TNT = register(
      "super_tnt", Builder.<SuperTNTEntity>of(SuperTNTEntity::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10)
   );
   public static final RegistryObject<EntityType<FormidibombEntity>> FORMIDIBOMB = register(
      "formidibomb", Builder.<FormidibombEntity>of(FormidibombEntity::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<CommandBlockEntity>> COMMAND_BLOCK = register(
      "command_block", Builder.<CommandBlockEntity>of(CommandBlockEntity::new, MobCategory.MISC).fireImmune().sized(1.0F, 1.0F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<WitheredSymbiontEntity>> WITHERED_SYMBIONT = register(
      "withered_symbiont", Builder.of(WitheredSymbiontEntity::new, MobCategory.MONSTER).fireImmune().sized(1.2F, 3.8F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<WitherStormHeadEntity>> WITHER_STORM_HEAD = register(
      "wither_storm_head", Builder.of(WitherStormHeadEntity::new, MobCategory.MONSTER).fireImmune().sized(5.0F, 5.0F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<TentacleEntity>> TENTACLE = register(
      "tentacle", Builder.of(TentacleEntity::new, MobCategory.MONSTER).fireImmune().sized(7.5F, 9.5F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<SickenedVillager>> SICKENED_VILLAGER = register(
      "sickened_villager", Builder.of(SickenedVillager::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedPhantom>> SICKENED_PHANTOM = register(
      "sickened_phantom", Builder.of(SickenedPhantom::new, MobCategory.MONSTER).sized(0.9F, 0.5F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedChicken>> SICKENED_CHICKEN = register(
      "sickened_chicken", Builder.of(SickenedChicken::new, MobCategory.MONSTER).sized(0.4F, 0.7F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<SickenedParrot>> SICKENED_PARROT = register(
      "sickened_parrot", Builder.of(SickenedParrot::new, MobCategory.MONSTER).sized(0.5F, 0.9F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedWolf>> SICKENED_WOLF = register(
      "sickened_wolf", Builder.of(SickenedWolf::new, MobCategory.MONSTER).sized(0.6F, 0.85F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<SickenedCat>> SICKENED_CAT = register(
      "sickened_cat", Builder.of(SickenedCat::new, MobCategory.MONSTER).sized(0.6F, 0.7F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedCow>> SICKENED_COW = register(
      "sickened_cow", Builder.of(SickenedCow::new, MobCategory.MONSTER).sized(0.9F, 1.4F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<SickenedPig>> SICKENED_PIG = register(
      "sickened_pig", Builder.of(SickenedPig::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<SickenedMushroomCow>> SICKENED_MUSHROOM_COW = register(
      "sickened_mushroom_cow", Builder.of(SickenedMushroomCow::new, MobCategory.MONSTER).sized(0.9F, 1.4F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<SickenedBee>> SICKENED_BEE = register(
      "sickened_bee", Builder.of(SickenedBee::new, MobCategory.MONSTER).sized(0.7F, 0.6F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedPillager>> SICKENED_PILLAGER = register(
      "sickened_pillager", Builder.of(SickenedPillager::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedVindicator>> SICKENED_VINDICATOR = register(
      "sickened_vindicator", Builder.of(SickenedVindicator::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<SickenedIronGolem>> SICKENED_IRON_GOLEM = register(
      "sickened_iron_golem",
      Builder.of(SickenedIronGolem::new, MobCategory.MONSTER).sized(1.4F, 2.7F).clientTrackingRange(10).fireImmune().immuneTo(new Block[]{Blocks.POWDER_SNOW})
   );
   public static final RegistryObject<EntityType<SickenedSnowGolem>> SICKENED_SNOW_GOLEM = register(
      "sickened_snow_golem",
      Builder.of(SickenedSnowGolem::new, MobCategory.MONSTER).immuneTo(new Block[]{Blocks.POWDER_SNOW}).sized(0.7F, 1.9F).clientTrackingRange(8)
   );
   public static final RegistryObject<EntityType<TaintedSlime>> TAINTED_SLIME = register(
      "tainted_slime", Builder.of(TaintedSlime::new, MobCategory.MONSTER).noSummon().sized(2.04F, 2.04F).clientTrackingRange(10)
   );
   public static final RegistryObject<EntityType<TentacleSpike>> TENTACLE_SPIKE = register(
      "tentacle_spike", Builder.<TentacleSpike>of(TentacleSpike::new, MobCategory.MISC).sized(0.5F, 1.4F).clientTrackingRange(6).updateInterval(2)
   );

   private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, Builder<T> builder) {
      return ENTITIES.register(id, () -> builder.build(new ResourceLocation("witherstormmod", id).toString()));
   }

   public static void addEntityAttributes(EntityAttributeCreationEvent event) {
      event.put(WITHER_STORM.get(), WitherStormEntity.createAttributes().build());
      event.put(WITHER_STORM_SEGMENT.get(), WitherStormSegmentEntity.createAttributes().build());
      event.put(SICKENED_ZOMBIE.get(), SickenedZombie.createAttributes().build());
      event.put(SICKENED_SKELETON.get(), SickenedSkeleton.createAttributes().build());
      event.put(SICKENED_SPIDER.get(), SickenedSpider.createAttributes().build());
      event.put(SICKENED_CREEPER.get(), SickenedCreeper.createAttributes().build());
      event.put(COMMAND_BLOCK.get(), CommandBlockEntity.createAttributes().build());
      event.put(WITHERED_SYMBIONT.get(), WitheredSymbiontEntity.createAttributes().build());
      event.put(WITHER_STORM_HEAD.get(), WitherStormHeadEntity.createAttributes().build());
      event.put(TENTACLE.get(), TentacleEntity.createAttributes().build());
      event.put(SICKENED_VILLAGER.get(), SickenedVillager.createAttributes().build());
      event.put(SICKENED_PHANTOM.get(), SickenedPhantom.createAttributes().build());
      event.put(SICKENED_CHICKEN.get(), SickenedChicken.createAttributes().build());
      event.put(SICKENED_PARROT.get(), SickenedParrot.createAttributes().build());
      event.put(SICKENED_WOLF.get(), SickenedWolf.createAttributes().build());
      event.put(SICKENED_CAT.get(), SickenedCat.createAttributes().build());
      event.put(SICKENED_COW.get(), SickenedCow.createAttributes().build());
      event.put(SICKENED_PIG.get(), SickenedPig.createAttributes().build());
      event.put(SICKENED_MUSHROOM_COW.get(), SickenedMushroomCow.createAttributes().build());
      event.put(SICKENED_BEE.get(), SickenedBee.createAttributes().build());
      event.put(SICKENED_PILLAGER.get(), SickenedPillager.createAttributes().build());
      event.put(SICKENED_VINDICATOR.get(), SickenedVindicator.createAttributes().build());
      event.put(SICKENED_IRON_GOLEM.get(), SickenedIronGolem.createAttributes().build());
      event.put(SICKENED_SNOW_GOLEM.get(), SickenedSnowGolem.createAttributes().build());
      event.put(TAINTED_SLIME.get(), TaintedSlime.createAttributes().build());
   }

   public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
      event.register(SICKENED_ZOMBIE.get(), Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, Operation.OR);
      event.register(SICKENED_SKELETON.get(), Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, Operation.OR);
      event.register(SICKENED_SPIDER.get(), Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, Operation.OR);
      event.register(SICKENED_CREEPER.get(), Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, Operation.OR);
   }
}

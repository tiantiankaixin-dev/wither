package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.blockentity.FireworkBundleBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.FormidibombBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.SuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.SuperSupportBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.WitheredPhlegmBlockEntity;

public class WitherStormModBlockEntityTypes {
   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "witherstormmod");
   public static final RegistryObject<BlockEntityType<FormidibombBlockEntity>> FORMIDIBOMB = BLOCK_ENTITIES.register(
      "formidibomb", () -> Builder.of(FormidibombBlockEntity::new, new Block[]{(Block)WitherStormModBlocks.FORMIDIBOMB.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<SuperBeaconBlockEntity>> SUPER_BEACON = BLOCK_ENTITIES.register(
      "super_beacon", () -> Builder.of(SuperBeaconBlockEntity::new, new Block[]{(Block)WitherStormModBlocks.SUPER_BEACON.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<SuperSupportBeaconBlockEntity>> SUPER_SUPPORT_BEACON = BLOCK_ENTITIES.register(
      "super_support_beacon",
      () -> Builder.of(SuperSupportBeaconBlockEntity::new, new Block[]{(Block)WitherStormModBlocks.SUPER_SUPPORT_BEACON.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<FireworkBundleBlockEntity>> FIREWORK_BUNDLE = BLOCK_ENTITIES.register(
      "firework_bundle", () -> Builder.of(FireworkBundleBlockEntity::new, new Block[]{(Block)WitherStormModBlocks.FIREWORK_BUNDLE.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<WitheredPhlegmBlockEntity>> WITHERED_PHLEGM = BLOCK_ENTITIES.register(
      "withered_phlegm",
      () -> Builder.of(WitheredPhlegmBlockEntity::new, new Block[]{(Block)WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get()}).build(null)
   );
}

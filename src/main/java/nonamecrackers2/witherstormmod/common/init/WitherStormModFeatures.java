package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.world.gen.feature.BowelsPodiumFeature;
import nonamecrackers2.witherstormmod.common.world.gen.feature.CommandBlockPodiumFeature;

public class WitherStormModFeatures {
   public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, "witherstormmod");
   public static final RegistryObject<CommandBlockPodiumFeature> COMMAND_BLOCK_PODIUM_FEATURE = FEATURES.register(
      "command_block_podium",
      () -> new CommandBlockPodiumFeature(NoneFeatureConfiguration.CODEC, new ResourceLocation("witherstormmod", "command_block_podium"))
   );
   public static final RegistryObject<BowelsPodiumFeature> BOWELS_PODIUM_FEATURE = FEATURES.register(
      "bowels_podium", () -> new BowelsPodiumFeature(NoneFeatureConfiguration.CODEC, new ResourceLocation("witherstormmod", "bowels_podium"))
   );

   public static Holder<ConfiguredFeature<?, ?>> getConfiguredFeature(ServerLevel level, ResourceLocation id) {
      return level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(ResourceKey.create(Registries.CONFIGURED_FEATURE, id));
   }
}

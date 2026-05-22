package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType.StructureTemplateType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.world.gen.feature.structure.BowelsStructure;
import nonamecrackers2.witherstormmod.common.world.gen.feature.structure.StormSpawnPlatformStructure;

public class WitherStormModStructures {
   public static final StructurePieceType PLATFORM = setTemplatePieceId(StormSpawnPlatformStructure.Piece::new);
   public static final DeferredRegister<StructureType<?>> STRUCTURE_FEATURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, "witherstormmod");
   public static final RegistryObject<StructureType<StormSpawnPlatformStructure>> STORM_SPAWN_PLATFORM = STRUCTURE_FEATURES.register(
      "storm_spawn_platform", () -> () -> StormSpawnPlatformStructure.CODEC
   );
   public static final RegistryObject<StructureType<BowelsStructure>> BOWELS = STRUCTURE_FEATURES.register("bowels", () -> () -> BowelsStructure.CODEC);

   private static StructurePieceType setTemplatePieceId(StructureTemplateType type) {
      return type;
   }

   public static void registerPieceTypes() {
      Registry.register(BuiltInRegistries.STRUCTURE_PIECE, new ResourceLocation("witherstormmod", "platform"), PLATFORM);
   }
}

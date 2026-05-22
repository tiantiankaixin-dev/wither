package nonamecrackers2.witherstormmod.common.event;

import java.util.List;
import java.util.Set;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import nonamecrackers2.witherstormmod.common.data.WitherStormModBlockStatesProvider;
import nonamecrackers2.witherstormmod.common.data.WitherStormModBlockTaintingRecipeProvider;
import nonamecrackers2.witherstormmod.common.data.WitherStormModItemModelProvider;
import nonamecrackers2.witherstormmod.common.data.WitherStormModLangProvider;
import nonamecrackers2.witherstormmod.common.data.WitherStormModMobConversionsProvider;
import nonamecrackers2.witherstormmod.common.data.WitherStormModRecipeProvider;
import nonamecrackers2.witherstormmod.common.data.loot.WitherStormModBlockLootProvider;
import nonamecrackers2.witherstormmod.common.data.loot.WitherStormModEntityLootProvider;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;

public class WitherStormModDataEvents {
   public static void gatherData(GatherDataEvent event) {
      DataGenerator generator = event.getGenerator();
      PackOutput output = generator.getPackOutput();
      ExistingFileHelper exFileHelper = event.getExistingFileHelper();
      generator.addProvider(event.includeClient(), new WitherStormModLangProvider(output));
      generator.addProvider(event.includeServer(), new WitherStormModRecipeProvider(output));
      generator.addProvider(event.includeClient(), new WitherStormModBlockStatesProvider(output, exFileHelper));
      generator.addProvider(event.includeClient(), new WitherStormModItemModelProvider(output, exFileHelper));
      generator.addProvider(
         event.includeServer(),
         new LootTableProvider(
            output,
            Set.of(),
            List.of(
               new SubProviderEntry(WitherStormModBlockLootProvider::new, LootContextParamSets.BLOCK),
               new SubProviderEntry(WitherStormModEntityLootProvider::new, LootContextParamSets.ENTITY)
            )
         )
      );
      generator.addProvider(event.includeServer(), new WitherStormModBlockTaintingRecipeProvider(output));
      generator.addProvider(event.includeServer(), new WitherStormModMobConversionsProvider(output));
   }

   public static void addResourceListeners(AddReloadListenerEvent event) {
      WorldTainting.initializeOrAddListeners(event::addListener);
   }
}

package nonamecrackers2.witherstormmod.api.common.data;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.resources.taint.MobConversion;

public abstract class MobConversionProvider implements DataProvider {
   private final Map<EntityType<?>, MobConversion> conversions = Maps.newHashMap();
   private final Path outputPath;

   public MobConversionProvider(PackOutput output, String modid) {
      this.outputPath = output.getOutputFolder(Target.DATA_PACK).resolve(modid).resolve("tainting/entity");
   }

   protected abstract void addConversions();

   protected void add(MobConversion conversion) {
      if (this.conversions.containsKey(conversion.from())) {
         throw new IllegalArgumentException("Type '" + conversion.from() + "' is already mapped");
      } else {
         this.conversions.put(conversion.from(), conversion);
      }
   }

   protected void add(EntityType<? extends Mob> from, EntityType<? extends Mob> to, boolean convertFromWitherSickness) {
      this.add(new MobConversion(from, to, convertFromWitherSickness));
   }

   protected void add(EntityType<? extends Mob> from, EntityType<? extends Mob> to) {
      this.add(from, to, true);
   }

   public CompletableFuture<?> run(CachedOutput output) {
      this.addConversions();
      return CompletableFuture.allOf(this.conversions.values().stream().map(conversion -> {
         JsonObject object = new JsonObject();
         ResourceLocation from = NeoBuiltInRegistries.ENTITY_TYPE.getKey(conversion.from());
         ResourceLocation to = NeoBuiltInRegistries.ENTITY_TYPE.getKey(conversion.to());
         object.addProperty("from", from.toString());
         object.addProperty("to", to.toString());
         object.addProperty("convert_from_sickness", conversion.canBeConvertedFromWitherSickness());
         return DataProvider.saveStable(output, object, this.outputPath.resolve(from.getPath() + "_to_" + to.getPath() + ".json"));
      }).toArray(CompletableFuture[]::new));
   }

   public String getName() {
      return "Mob conversions";
   }
}

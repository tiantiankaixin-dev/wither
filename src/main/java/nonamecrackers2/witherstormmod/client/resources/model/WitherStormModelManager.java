package nonamecrackers2.witherstormmod.client.resources.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.client.model.obj.ObjModel;

public class WitherStormModelManager extends SimpleJsonResourceReloadListener {
   private static final Gson GSON = new GsonBuilder().create();
   public static final WitherStormModelManager INSTANCE = new WitherStormModelManager();
   private Map<ModelLayerLocation, ObjModel> models = Maps.newHashMap();

   public WitherStormModelManager() {
      super(GSON, "models/wither_storm_mass");
   }

   protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
      this.models.clear();
   }

   public boolean hasObjModel(ModelLayerLocation location) {
      return this.models.containsKey(location);
   }

   @Nullable
   public ObjModel getModel(ModelLayerLocation location) {
      return this.models.get(location);
   }

   public Map<ModelLayerLocation, ObjModel> getModels() {
      return ImmutableMap.copyOf(this.models);
   }
}

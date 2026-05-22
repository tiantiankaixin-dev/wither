package nonamecrackers2.witherstormmod.client.resources.texture;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import nonamecrackers2.witherstormmod.client.renderer.entity.AbstractWitherStormRenderer;

public record TextureSet(ResourceLocation invulnerable, ResourceLocation main, ResourceLocation emissiveDecal, ResourceLocation debrisRing) {
   public static final TextureSet DEFAULT = builder().build();

   public static TextureSet.Builder builder() {
      return new TextureSet.Builder();
   }

   public static TextureSet fromJson(JsonObject object) throws JsonSyntaxException {
      TextureSet.Builder builder = builder();
      applyTexture(builder::setInvulnerable, object, "invulnerable");
      applyTexture(builder::setMain, object, "main");
      applyTexture(builder::setEmissiveDecal, object, "emissive_decal");
      applyTexture(builder::setDebrisRing, object, "debris_ring");
      return builder.build();
   }

   private static void applyTexture(Consumer<ResourceLocation> consumer, JsonObject object, String path) throws JsonSyntaxException {
      if (object.has(path)) {
         String rawId = GsonHelper.getAsString(object, path);
         ResourceLocation id = ResourceLocation.tryParse(rawId);
         if (id == null) {
            throw new JsonSyntaxException("Not a valid id: '" + rawId + "'");
         }

         consumer.accept(id);
      }
   }

   public static class Builder {
      private ResourceLocation invulnerable = AbstractWitherStormRenderer.WITHER_STORM_INVULNERABLE_LOCATION;
      private ResourceLocation main = AbstractWitherStormRenderer.WITHER_STORM_LOCATION;
      private ResourceLocation emissiveDecal = AbstractWitherStormRenderer.EMISSIVE_DECAL;
      private ResourceLocation debrisRing = AbstractWitherStormRenderer.DEBRIS_RING;

      private Builder() {
      }

      public TextureSet.Builder setInvulnerable(ResourceLocation invulnerable) {
         this.invulnerable = invulnerable;
         return this;
      }

      public TextureSet.Builder setMain(ResourceLocation main) {
         this.main = main;
         return this;
      }

      public TextureSet.Builder setEmissiveDecal(ResourceLocation emissiveDecal) {
         this.emissiveDecal = emissiveDecal;
         return this;
      }

      public TextureSet.Builder setDebrisRing(ResourceLocation debrisRing) {
         this.debrisRing = debrisRing;
         return this;
      }

      public TextureSet build() {
         return new TextureSet(this.invulnerable, this.main, this.emissiveDecal, this.debrisRing);
      }
   }
}

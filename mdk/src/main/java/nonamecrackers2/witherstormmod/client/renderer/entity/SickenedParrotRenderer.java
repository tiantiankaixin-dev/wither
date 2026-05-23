package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Parrot;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;

public class SickenedParrotRenderer extends ParrotRenderer {
   public static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_parrot.png");
   public static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_parrot_emissive.png");

   public SickenedParrotRenderer(Context context) {
      super(context);
      this.addLayer(new EyesLayer<Parrot, ParrotModel>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedParrotRenderer.EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(Parrot parrot) {
      return TEXTURE;
   }

   protected boolean isShaking(Parrot entity) {
      return super.isShaking(entity) || ((WitherSickened)entity).isConverting();
   }
}

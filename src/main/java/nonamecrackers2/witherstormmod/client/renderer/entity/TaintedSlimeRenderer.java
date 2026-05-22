package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.TaintedSlimeModel;
import nonamecrackers2.witherstormmod.common.entity.TaintedSlime;

public class TaintedSlimeRenderer extends MobRenderer<TaintedSlime, TaintedSlimeModel<TaintedSlime>> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/tainted_slime/tainted_slime.png");

   public TaintedSlimeRenderer(Context context) {
      super(context, new TaintedSlimeModel(context.bakeLayer(WitherStormModRenderers.TAINTED_SLIME)), 0.25F);
   }

   public ResourceLocation getTextureLocation(TaintedSlime slime) {
      return TEXTURE;
   }
}

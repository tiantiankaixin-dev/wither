package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.AbstractWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormSegmentModel;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;

public class WitherStormSegmentRenderer extends AbstractWitherStormRenderer<WitherStormSegmentEntity, AbstractWitherStormModel<WitherStormSegmentEntity>> {
   private final WitherStormSegmentModel<WitherStormSegmentEntity> segmentModel;

   public WitherStormSegmentRenderer(Context context) {
      this(context, new WitherStormSegmentModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_SEGMENT)));
   }

   private WitherStormSegmentRenderer(Context context, WitherStormSegmentModel<WitherStormSegmentEntity> base) {
      super(context, base);
      this.segmentModel = base;
   }

   public AbstractWitherStormModel<WitherStormSegmentEntity> fetchModel(WitherStormSegmentEntity entity) {
      return this.segmentModel;
   }

   public int getPulseAmount(WitherStormSegmentEntity entity) {
      return (int)((float)entity.getPhase() * 5.0F / 2.0F * (WitherStormModConfig.CLIENT.lowResModels.get() ? 3.0F : 1.0F));
   }
}

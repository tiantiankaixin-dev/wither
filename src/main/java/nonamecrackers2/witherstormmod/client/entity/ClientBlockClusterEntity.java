package nonamecrackers2.witherstormmod.client.entity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;

public class ClientBlockClusterEntity extends BlockClusterEntity {
   private final ClientBlockClusterEntity.BlockClusterWorld blockGetter;
   private Map<RenderType, Map<BlockPos, BlockState>> toRender = new LinkedHashMap<>();
   private Map<BlockPos, BlockState> tilesToRender = new LinkedHashMap<>();
   @Nullable
   private String toRenderUniqueId;
   public float fadeAmount = 1.0F;
   private float fadeAmountO = 1.0F;

   public ClientBlockClusterEntity(EntityType<? extends BlockClusterEntity> entityType, Level world) {
      super(entityType, world);
      this.blockGetter = new ClientBlockClusterEntity.BlockClusterWorld(world, this);
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> parameter) {
      super.onSyncedDataUpdated(parameter);
      if (parameter.equals(BlockClusterEntity.BLOCKS)) {
         this.toRender.clear();
         this.tilesToRender.clear();

         for (Entry<BlockPos, BlockState> entry : this.getBlocks().entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();
            if (state.getRenderShape() == RenderShape.ENTITYBLOCK_ANIMATED) {
               this.tilesToRender.put(pos, state);
            } else if (state.getRenderShape() == RenderShape.MODEL) {
               BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
               BakedModel model = dispatcher.getBlockModel(state);
               ChunkRenderTypeSet blockRenderTypes = model.getRenderTypes(state, RandomSource.create(state.getSeed(this.getStartPos())), ModelData.EMPTY);

               for (RenderType type : RenderType.chunkBufferLayers()) {
                  if (blockRenderTypes.contains(type)) {
                     Map<BlockPos, BlockState> map = this.toRender.computeIfAbsent(type, t -> new LinkedHashMap<>());
                     map.put(pos, state);
                  }
               }
            }
         }

         this.toRenderUniqueId = this.toRender.toString();
      } else if (parameter.equals(BlockClusterEntity.FADE_POINT)) {
         this.calculateFade();
         this.fadeAmountO = this.fadeAmount;
      }
   }

   public Map<RenderType, Map<BlockPos, BlockState>> toRender() {
      return this.toRender;
   }

   public Map<BlockPos, BlockState> tilesToRender() {
      return this.tilesToRender;
   }

   @Nullable
   public String getToRenderUniqueId() {
      return this.toRenderUniqueId;
   }

   public BlockAndTintGetter getBlockGetter() {
      return this.blockGetter;
   }

   @Override
   public void tick() {
      super.tick();
      this.calculateFade();
   }

   private void calculateFade() {
      if ((Boolean)WitherStormModConfig.CLIENT.blockClusterRendering.get() && this.getShakeTime() <= 0) {
         this.fadeAmountO = this.fadeAmount;
         BlockPos point = this.getFadePos();
         if (point != null) {
            double distanceFromCreationToFade = Math.sqrt(this.getStartPos().distSqr(point)) - (double)this.getFadeDistanceOffset();
            double distance = Math.max(0.0, Vec3.atCenterOf(point).distanceTo(this.position()) - (double)this.getFadeDistanceOffset());
            this.fadeAmount = Math.min(1.0F, (float)distance / Math.min((float)distanceFromCreationToFade, this.getFadeStrength()));
         }
      }
   }

   public float lerpFadeAmount(float partialTicks) {
      return Mth.lerp(partialTicks, this.fadeAmountO, this.fadeAmount);
   }

   public static class BlockClusterWorld implements BlockAndTintGetter {
      private final Level wrapped;
      private final BlockClusterEntity cluster;

      public BlockClusterWorld(Level wrapped, BlockClusterEntity cluster) {
         this.wrapped = wrapped;
         this.cluster = cluster;
      }

      public BlockEntity getBlockEntity(BlockPos pos) {
         return null;
      }

      public BlockState getBlockState(BlockPos pos) {
         BlockState state = this.cluster.getBlocks().get(pos.subtract(this.cluster.getStartPos()));
         if (state == null) {
            state = Blocks.AIR.defaultBlockState();
         }

         return state;
      }

      public FluidState getFluidState(BlockPos pos) {
         return this.getBlockState(pos).getFluidState();
      }

      public int getHeight() {
         return this.wrapped.getHeight();
      }

      public int getMinBuildHeight() {
         return this.wrapped.getMinBuildHeight();
      }

      public float getShade(Direction direction, boolean p_45523_) {
         return this.wrapped.getShade(direction, p_45523_);
      }

      public LevelLightEngine getLightEngine() {
         return this.wrapped.getLightEngine();
      }

      public int getRawBrightness(BlockPos pos, int skyOffset) {
         return 15; // TODO: fix shader compat
      }

      public int getBrightness(LightLayer layer, BlockPos pos) {
         return 15; // TODO: fix shader compat
      }

      public int getBlockTint(BlockPos pos, ColorResolver resolver) {
         return this.wrapped.getBlockTint(pos, resolver);
      }
   }
}

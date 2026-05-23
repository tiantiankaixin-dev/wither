package nonamecrackers2.witherstormmod.client.instancing;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.VertexBuffer.Usage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import nonamecrackers2.witherstormmod.mixin.MixinBufferBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.MemoryUtil;

public class AsyncBufferedInstance extends BufferedInstance {
   private static final Logger LOGGER = LogManager.getLogger();
   @Nullable
   private CompletableFuture<BufferBuilder> bufferBuilder;

   public AsyncBufferedInstance(RenderType type, BufferedInstance.Bufferable bufferer, Supplier<Boolean> shouldRemove) {
      super(type, bufferer, shouldRemove);
   }

   public void checkBufferBuilderStatus() {
      if (this.bufferBuilder != null && this.bufferBuilder.isDone()) {
         try {
            BufferBuilder builder = this.bufferBuilder.get();
            if (this.buffer != null) {
               this.buffer.close();
            }

            this.buffer = new VertexBuffer(Usage.STATIC);
            RenderedBuffer rendered = builder.end();
            this.buffer.bind();
            this.buffer.upload(rendered);
            VertexBuffer.unbind();
            builder.clear();
            MixinBufferBuilder mixin = (MixinBufferBuilder)builder;
            MemoryUtil.memFree(mixin.witherstormmod$getBuffer());
            mixin.witherstormmod$setBuffer(null);
         } catch (ExecutionException | InterruptedException var4) {
            LOGGER.error("Failed to get BufferBuilder", var4);
         }

         this.bufferBuilder = null;
      }
   }

   @Override
   public void buildBuffer(PoseStack stack, ExecutorService pool) {
      this.bufferBuilder = CompletableFuture.<BufferBuilder>supplyAsync(() -> {
         RenderType type = this.getRenderType();
         BufferBuilder builder = new BufferBuilder(512);
         builder.begin(type.mode(), type.format());
         this.bufferInto(stack, builder, 15728880, OverlayTexture.NO_OVERLAY, -1);
         return builder;
      }, pool).handle((b, e) -> {
         if (e != null) {
            LOGGER.error("Failed to build buffer asynchronously", e);
         }

         b.clear();
         return (BufferBuilder)b;
      });
      this.dirty = false;
   }
}

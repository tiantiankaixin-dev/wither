package nonamecrackers2.witherstormmod.client.instancing;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexBuffer.Usage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsyncBufferedInstance extends BufferedInstance {
   private static final Logger LOGGER = LogManager.getLogger();
   @Nullable
   private CompletableFuture<MeshData> meshData;

   public AsyncBufferedInstance(RenderType type, BufferedInstance.Bufferable bufferer, Supplier<Boolean> shouldRemove) {
      super(type, bufferer, shouldRemove);
   }

   public void checkBufferBuilderStatus() {
      if (this.meshData != null && this.meshData.isDone()) {
         try {
            MeshData rendered = this.meshData.get();
            if (rendered != null) {
               if (this.buffer != null) {
                  this.buffer.close();
               }

               this.buffer = new VertexBuffer(Usage.STATIC);
               this.buffer.bind();
               this.buffer.upload(rendered);
               VertexBuffer.unbind();
            }
         } catch (ExecutionException | InterruptedException var4) {
            LOGGER.error("Failed to get BufferBuilder", var4);
         }

         this.meshData = null;
      }
   }

   @Override
   public void buildBuffer(PoseStack stack, ExecutorService pool) {
      this.meshData = CompletableFuture.<MeshData>supplyAsync(() -> {
         RenderType type = this.getRenderType();
         ByteBufferBuilder byteBuffer = new ByteBufferBuilder(512);
         BufferBuilder builder = new BufferBuilder(byteBuffer, type.mode(), type.format());
         this.bufferInto(stack, builder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
         return builder.buildOrThrow();
      }, pool).handle((b, e) -> {
         if (e != null) {
            LOGGER.error("Failed to build buffer asynchronously", e);
         }

         return b;
      });
      this.dirty = false;
   }
}

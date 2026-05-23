package nonamecrackers2.witherstormmod.client.instancing;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.VertexBuffer.Usage;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class BufferedInstance {
   @Nullable
   protected VertexBuffer buffer;
   private final RenderType type;
   private BufferedInstance.Bufferable bufferer;
   protected boolean dirty = true;
   @Nullable
   private final Supplier<Boolean> shouldRemove;
   private boolean wasRenderedLastFrame = true;

   public BufferedInstance(RenderType type, BufferedInstance.Bufferable bufferer, @Nullable Supplier<Boolean> shouldRemove) {
      this.type = type;
      this.bufferer = bufferer;
      this.shouldRemove = shouldRemove;
   }

   public void bufferInto(PoseStack stack, VertexConsumer consumer, int packedLight, int overlayTexture, float r, float g, float b, float a) {
      this.bufferer.bufferInto(stack, consumer, packedLight, overlayTexture, r, g, b, a);
   }

   public RenderType getRenderType() {
      return this.type;
   }

   @Nullable
   public VertexBuffer getBuffer() {
      return this.buffer;
   }

   public void close() {
      if (this.buffer != null) {
         this.buffer.close();
      }
   }

   public boolean requiresComputing() {
      return this.dirty;
   }

   public void markDirty() {
      this.dirty = true;
   }

   public void buildBuffer(PoseStack stack, ExecutorService pool) {
      if (this.buffer != null) {
         this.buffer.close();
      }

      this.buffer = new VertexBuffer(Usage.STATIC);
      RenderType type = this.getRenderType();
      BufferBuilder buffer = Tesselator.getInstance().getBuilder();
      buffer.begin(type.mode(), type.format());
      this.bufferInto(stack, buffer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      RenderedBuffer rendered = buffer.end();
      this.buffer.bind();
      this.buffer.upload(rendered);
      VertexBuffer.unbind();
      this.dirty = false;
   }

   public void setBufferer(BufferedInstance.Bufferable bufferer) {
      this.bufferer = bufferer;
   }

   public boolean shouldRemove() {
      return this.shouldRemove != null ? this.shouldRemove.get() : false;
   }

   public void setRenderedLastFrame(boolean flag) {
      this.wasRenderedLastFrame = flag;
   }

   public boolean wasRenderedLastFrame() {
      return this.wasRenderedLastFrame;
   }

   public boolean hasRemoveSupplier() {
      return this.shouldRemove != null;
   }

   public interface Bufferable {
      void bufferInto(PoseStack var1, VertexConsumer var2, int var3, int var4, float var5, float var6, float var7, float var8);
   }
}

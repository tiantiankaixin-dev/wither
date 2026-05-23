package nonamecrackers2.crackerslib.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import nonamecrackers2.crackerslib.client.gui.widget.Widget3D;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class Screen3D extends Screen {
   protected final float farPlane;
   protected final float zoomConstant;
   protected float camRotX = 225.0F;
   protected float camRotY = 45.0F;
   protected float zoom = 1.0F;
   protected Vector3f offset = new Vector3f(0.0F, 0.0F, 0.0F);
   @Nullable
   protected Matrix4f poseMatrix;
   @Nullable
   protected Vector3f hitPos;
   @Nullable
   protected Vector3f lastDragPos;
   protected boolean renderOrigin;
   protected int moveForTime;
   protected float moveFor;
   @Nullable
   protected Vector3f moveFrom;
   @Nullable
   protected Supplier<Vector3f> moveTo;
   protected float initialZoom;
   protected float finalZoom;

   protected Screen3D(Component title, float zoomConstant, float farPlane) {
      super(title);
      this.farPlane = farPlane;
      this.zoomConstant = zoomConstant;
   }

   protected void renderOrigin(boolean flag) {
      this.renderOrigin = flag;
   }

   public boolean m_7979_(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
      if (!super.m_7979_(pMouseX, pMouseY, pButton, pDragX, pDragY)) {
         if (pButton == 1) {
            if (this.canRotate()) {
               this.camRotX = Mth.m_14036_(this.camRotX + (float)pDragY, 90.0F, 270.0F);
               this.camRotY = (float)Mth.m_14175_(this.camRotY + pDragX);
               this.onRotate();
            }
         } else if (pButton == 0 && this.canMove()) {
            Vector3f move = new Vector3f((float)(-pDragX) / (this.zoom * this.zoomConstant), (float)pDragY / (this.zoom * this.zoomConstant), 0.0F);
            move.rotate(Axis.f_252529_.m_252977_(this.camRotX));
            move.rotate(Axis.f_252392_.m_252977_(this.camRotY));
            this.offset.add(move);
            this.onMove();
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean m_6050_(double pMouseX, double pMouseY, double pDelta) {
      if (!super.m_6050_(pMouseX, pMouseY, pDelta)) {
         if (this.canZoom()) {
            this.zoom = Math.max(this.zoom + (float)pDelta * (this.zoom / 10.0F), 1.0F);
            this.onZoom();
         }

         return false;
      } else {
         return true;
      }
   }

   public void m_88315_(GuiGraphics stack, int pMouseX, int pMouseY, float pPartialTick) {
      if (this.moveFor > 0.0F) {
         this.moveFor = this.moveFor - pPartialTick * Math.max(this.moveFor / this.moveForTime, 1.0E-4F);
         if (this.moveForTime > 0 && this.moveTo != null && this.moveFrom != null) {
            Vector3f finalPos = this.moveTo.get();
            if (finalPos != null) {
               float transition = this.moveFor / this.moveForTime;
               float x = Mth.m_14179_(transition, finalPos.x, this.moveFrom.x);
               float y = Mth.m_14179_(transition, finalPos.y, this.moveFrom.y);
               float z = Mth.m_14179_(transition, finalPos.z, this.moveFrom.z);
               this.offset = new Vector3f(x, y, z);
               this.zoom = Mth.m_14179_(transition, this.finalZoom, this.initialZoom);
            }
         }

         if (this.moveFor <= 0.0F) {
            this.moveFrom = null;
            this.moveForTime = 0;
         }
      } else if (this.moveTo != null) {
         this.offset = this.moveTo.get();
      }

      Quaternionf rot = new Quaternionf().rotateX(this.camRotX * (float) (Math.PI / 180.0)).rotateY((float) Math.PI + this.camRotY * (float) (Math.PI / 180.0));
      Matrix4f prevProjMat = RenderSystem.getProjectionMatrix();
      Window window = this.f_96541_.m_91268_();
      Matrix4f matrix4f = new Matrix4f()
         .setOrtho(0.0F, (float)(window.m_85441_() / window.m_85449_()), (float)(window.m_85442_() / window.m_85449_()), 0.0F, 0.0F, this.farPlane);
      RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.f_276633_);
      PoseStack modelViewStack = RenderSystem.getModelViewStack();
      modelViewStack.m_85836_();
      modelViewStack.m_166856_();
      RenderSystem.applyModelViewMatrix();
      stack.m_280168_().m_85836_();
      stack.m_280168_().m_85837_(this.f_96543_ / 2, this.f_96544_ / 2, 0.0);
      stack.m_280168_().m_252931_(new Matrix4f().scaling(this.zoom * this.zoomConstant, this.zoom * this.zoomConstant, -1.0F));
      stack.m_280168_().m_85837_(0.0, 0.0, this.farPlane / 2.0F);
      stack.m_280168_().m_252781_(rot);
      stack.m_280168_().m_252880_(this.offset.x, this.offset.y, this.offset.z);
      Lighting.m_166384_();
      BufferSource bufferSource = this.f_96541_.m_91269_().m_110104_();
      if (this.renderOrigin) {
         VertexConsumer consumer = bufferSource.m_6299_(RenderType.m_110504_());
         Matrix4f pose = stack.m_280168_().m_85850_().m_252922_();
         Matrix3f normal = stack.m_280168_().m_85850_().m_252943_();
         consumer.m_252986_(pose, 0.0F, 0.0F, 0.0F).m_85950_(0.0F, 1.0F, 0.0F, 1.0F).m_252939_(normal, 0.0F, 1.0F, 0.0F).m_5752_();
         consumer.m_252986_(pose, 0.0F, 1.0F, 0.0F).m_85950_(0.0F, 1.0F, 0.0F, 1.0F).m_252939_(normal, 0.0F, 1.0F, 0.0F).m_5752_();
         consumer.m_252986_(pose, 0.0F, 0.0F, 0.0F).m_85950_(1.0F, 0.0F, 0.0F, 1.0F).m_252939_(normal, 1.0F, 0.0F, 0.0F).m_5752_();
         consumer.m_252986_(pose, 1.0F, 0.0F, 0.0F).m_85950_(1.0F, 0.0F, 0.0F, 1.0F).m_252939_(normal, 1.0F, 0.0F, 0.0F).m_5752_();
         consumer.m_252986_(pose, 0.0F, 0.0F, 0.0F).m_85950_(0.0F, 0.0F, 1.0F, 1.0F).m_252939_(normal, 0.0F, 0.0F, 1.0F).m_5752_();
         consumer.m_252986_(pose, 0.0F, 0.0F, 1.0F).m_85950_(0.0F, 0.0F, 1.0F, 1.0F).m_252939_(normal, 0.0F, 0.0F, 1.0F).m_5752_();
      }

      this.poseMatrix = stack.m_280168_().m_85850_().m_252922_();
      this.render3D(stack.m_280168_(), bufferSource, pMouseX, pMouseY, this.f_96541_.getPartialTick());
      bufferSource.m_109911_();

      for (Renderable renderable : this.f_169369_) {
         if (renderable instanceof Widget3D widget) {
            widget.renderAs3D(stack.m_280168_(), bufferSource, pMouseX, pMouseY, this.f_96541_.getPartialTick());
         }
      }

      RenderSystem.clear(256, Minecraft.f_91002_);
      bufferSource.m_109911_();
      stack.m_280168_().m_85849_();
      RenderSystem.clear(256, Minecraft.f_91002_);
      RenderSystem.setProjectionMatrix(prevProjMat, VertexSorting.f_276633_);
      modelViewStack.m_85849_();
      RenderSystem.applyModelViewMatrix();
      super.m_88315_(stack, pMouseX, pMouseY, pPartialTick);
   }

   protected void render3D(PoseStack stack, MultiBufferSource buffers, int mouseX, int mouseY, float partialTick) {
   }

   protected boolean canZoom() {
      return this.moveFor <= 0.0F;
   }

   protected boolean canMove() {
      return this.moveFor <= 0.0F;
   }

   protected boolean canRotate() {
      return true;
   }

   protected void onZoom() {
   }

   protected void onMove() {
      this.moveTo = null;
   }

   protected void onRotate() {
   }

   protected void lerpTo(int time, Supplier<Vector3f> pos, float zoom) {
      this.moveFor = time;
      this.moveForTime = time;
      this.moveTo = pos;
      this.moveFrom = this.offset;
      this.initialZoom = this.zoom;
      this.finalZoom = zoom;
   }

   public static void renderIcon(
      PoseStack stack,
      Vector3f pos,
      MultiBufferSource buffer,
      ResourceLocation tex,
      float camRotX,
      float camRotY,
      float zoom,
      float size,
      float r,
      float g,
      float b
   ) {
      stack.m_85836_();
      stack.m_252880_(pos.x, pos.y, pos.z);
      stack.m_85841_(1.0F / zoom, 1.0F / zoom, 1.0F / zoom);
      stack.m_252781_(Axis.f_252392_.m_252977_(camRotY));
      stack.m_252781_(Axis.f_252529_.m_252977_(camRotX));
      stack.m_85841_(1.0F, -1.0F, 1.0F);
      stack.m_85837_(0.0, 0.0, 10.0);
      Matrix4f pose = stack.m_85850_().m_252922_();
      Matrix3f normal = stack.m_85850_().m_252943_();
      VertexConsumer consumer = buffer.m_6299_(RenderType.m_110452_(tex));
      consumer.m_252986_(pose, size, -size, size)
         .m_85950_(r, g, b, 1.0F)
         .m_7421_(0.0F, 1.0F)
         .m_86008_(OverlayTexture.f_118083_)
         .m_85969_(15728880)
         .m_252939_(normal, 0.0F, 1.0F, 0.0F)
         .m_5752_();
      consumer.m_252986_(pose, -size, -size, size)
         .m_85950_(r, g, b, 1.0F)
         .m_7421_(1.0F, 1.0F)
         .m_86008_(OverlayTexture.f_118083_)
         .m_85969_(15728880)
         .m_252939_(normal, 0.0F, 1.0F, 0.0F)
         .m_5752_();
      consumer.m_252986_(pose, -size, size, size)
         .m_85950_(r, g, b, 1.0F)
         .m_7421_(1.0F, 0.0F)
         .m_86008_(OverlayTexture.f_118083_)
         .m_85969_(15728880)
         .m_252939_(normal, 0.0F, 1.0F, 0.0F)
         .m_5752_();
      consumer.m_252986_(pose, size, size, size)
         .m_85950_(r, g, b, 1.0F)
         .m_7421_(0.0F, 0.0F)
         .m_86008_(OverlayTexture.f_118083_)
         .m_85969_(15728880)
         .m_252939_(normal, 0.0F, 1.0F, 0.0F)
         .m_5752_();
      stack.m_85849_();
   }
}

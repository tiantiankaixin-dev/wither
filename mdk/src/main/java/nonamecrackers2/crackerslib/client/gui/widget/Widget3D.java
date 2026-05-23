package nonamecrackers2.crackerslib.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import nonamecrackers2.crackerslib.client.util.RenderUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Widget3D extends AbstractWidget {
   @Nullable
   protected Matrix4f poseMatrix;
   protected Vector2f screenPos;
   protected Vector3f pos;

   public Widget3D(Vector3f pos, int screenX, int screenY, int width, int height, Component name) {
      super(screenX, screenY, width, height, name);
      this.pos = pos;
   }

   public void setPos(Vector3f pos) {
      this.pos = pos;
   }

   protected void updatePoseMatrix(Matrix4f poseMatrix) {
      this.poseMatrix = poseMatrix;
   }

   protected final Vector2f convertPosToScreenCoord(Vector3f pos) {
      Objects.requireNonNull(this.poseMatrix, "Previous 3D pose matrix is null!");
      return RenderUtil.getScreenCoordinatesFromWorldPos(this.poseMatrix, pos);
   }

   protected final Vector3f convertPosToScreenCordWithZDist(Vector3f pos) {
      Objects.requireNonNull(this.poseMatrix, "Previous 3D pose matrix is null!");
      return RenderUtil.getScreenCoordinatesFromWorldPosWithZDist(this.poseMatrix, pos);
   }

   public void renderAs3D(PoseStack stack, MultiBufferSource buffers, int mouseX, int mouseY, float partialTick) {
      this.updatePoseMatrix(stack.m_85850_().m_252922_());
      this.screenPos = this.convertPosToScreenCoord(this.pos);
      this.updatePos();
   }

   protected void updatePos() {
      this.m_252865_((int)this.screenPos.x);
      this.m_253211_((int)this.screenPos.y);
   }

   protected static void blit(PoseStack stack, float x, float y, int blitOffset, float width, float height, float u1, float v1, float u2, float v2) {
      Matrix4f matrix4f = stack.m_85850_().m_252922_();
      RenderSystem.setShader(GameRenderer::m_172817_);
      BufferBuilder bufferbuilder = Tesselator.m_85913_().m_85915_();
      bufferbuilder.m_166779_(Mode.QUADS, DefaultVertexFormat.f_85817_);
      bufferbuilder.m_252986_(matrix4f, x, y, blitOffset).m_7421_(u1, v1).m_5752_();
      bufferbuilder.m_252986_(matrix4f, x, y + height, blitOffset).m_7421_(u1, v2).m_5752_();
      bufferbuilder.m_252986_(matrix4f, x + width, y + height, blitOffset).m_7421_(u2, v2).m_5752_();
      bufferbuilder.m_252986_(matrix4f, x + width, y, blitOffset).m_7421_(u2, v1).m_5752_();
      BufferUploader.m_231202_(bufferbuilder.m_231175_());
   }
}

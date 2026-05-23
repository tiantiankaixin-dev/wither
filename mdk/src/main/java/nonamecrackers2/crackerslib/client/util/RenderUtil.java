package nonamecrackers2.crackerslib.client.util;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import nonamecrackers2.crackerslib.mixin.MixinGameRendererAccessor;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class RenderUtil {
   private static boolean extendFarPlane;
   private static Matrix4f previousProjMat;

   private RenderUtil() {
   }

   public static void setClipPlanes(Matrix4f mat, float near, float far) {
      mat.set(2, 2, -((far + near) / (far - near))).set(3, 2, -(2.0F * far * near / (far - near)));
   }

   public static void renderCenteredWordWrap(GuiGraphics stack, Font font, FormattedText text, int x, int y, int width, int color) {
      List<FormattedCharSequence> texts = font.m_92923_(text, width);
      int totalHeight = texts.size() * (9 + 2);

      for (int i = 0; i < texts.size(); i++) {
         stack.m_280364_(font, texts.get(i), x, y + i * 9 + 2 - totalHeight / 2, color);
      }
   }

   public static void renderHorizontallyCenteredWordWrap(GuiGraphics stack, Font font, FormattedText text, int x, int y, int width, int color) {
      List<FormattedCharSequence> texts = font.m_92923_(text, width);

      for (int i = 0; i < texts.size(); i++) {
         stack.m_280364_(font, texts.get(i), x, y + i * 9 + 2, color);
      }
   }

   public static void line(GuiGraphics stack, Vector2f start, Vector2f end, int blitOffset, float lineWidth, float r, float g, float b, float a) {
      Vector2f normal = start.sub(end, new Vector2f()).normalize();
      Matrix4f matrix4f = stack.m_280168_().m_85850_().m_252922_();
      Matrix3f matrix3f = stack.m_280168_().m_85850_().m_252943_();
      BufferBuilder bufferbuilder = Tesselator.m_85913_().m_85915_();
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::m_172757_);
      RenderSystem.lineWidth(lineWidth);
      bufferbuilder.m_166779_(Mode.LINES, DefaultVertexFormat.f_166851_);
      if (normal.y < -0.008F) {
         bufferbuilder.m_252986_(matrix4f, start.x, start.y, blitOffset).m_85950_(r, g, b, a).m_252939_(matrix3f, normal.x, normal.y, 0.0F).m_5752_();
         bufferbuilder.m_252986_(matrix4f, end.x, end.y, blitOffset).m_85950_(r, g, b, a).m_252939_(matrix3f, normal.x, normal.y, 0.0F).m_5752_();
      } else {
         bufferbuilder.m_252986_(matrix4f, end.x, end.y, blitOffset).m_85950_(r, g, b, a).m_252939_(matrix3f, normal.x, normal.y, 0.0F).m_5752_();
         bufferbuilder.m_252986_(matrix4f, start.x, start.y, blitOffset).m_85950_(r, g, b, a).m_252939_(matrix3f, normal.x, normal.y, 0.0F).m_5752_();
      }

      BufferUploader.m_231202_(bufferbuilder.m_231175_());
      RenderSystem.disableBlend();
   }

   public static Vector3f getWorldPosFromScreenPos(Matrix4f mat, int screenX, int screenY, float z) {
      Matrix4f inverse = new Matrix4f(mat).invert();
      Vector4f vec4 = new Vector4f(screenX, screenY, z, 1.0F).mul(inverse);
      float w = 1.0F / vec4.w;
      return new Vector3f(vec4.x * w, vec4.y * w, vec4.z * w);
   }

   public static Vector2f getScreenCoordinatesFromWorldPos(Matrix4f mat, Vector3f pos) {
      Vector4f vector4f = mat.transform(new Vector4f(pos.x, pos.y, pos.z, 1.0F));
      return new Vector2f(vector4f.x, vector4f.y);
   }

   public static Vector3f getScreenCoordinatesFromWorldPosWithZDist(Matrix4f mat, Vector3f pos) {
      Vector4f vector4f = mat.transform(new Vector4f(pos.x, pos.y, pos.z, 1.0F));
      return new Vector3f(vector4f.x, vector4f.y, vector4f.z);
   }

   public static float getScreenZCoord(Matrix4f mat, Vector3f pos) {
      return mat.transform(new Vector4f(pos.x, pos.y, pos.z, 1.0F)).z();
   }

   public static void renderCubeSphere(
      int subdivision, float radius, PoseStack stack, VertexConsumer consumer, int packedLight, int overlayTexture, boolean useNormals
   ) {
      int pointsPerRow = (int)(Math.pow(2.0, subdivision) + 1.0);
      List<Vector3f> unitVertices = generateCubeSpherePosXVertices(pointsPerRow);
      List<Vector3f> vertices = Lists.newArrayList();
      List<Vector3f> normals = Lists.newArrayList();
      List<Vector2f> texCoords = Lists.newArrayList();
      List<Integer> indices = Lists.newArrayList();
      int k = 0;

      for (int i = 0; i < pointsPerRow; i++) {
         int k1 = i * pointsPerRow;
         int k2 = k1 + pointsPerRow;
         float t = i / (pointsPerRow - 1.0F);

         for (int j = 0; j < pointsPerRow; k2++) {
            Vector3f vertex = unitVertices.get(k);
            float x = vertex.x;
            float y = vertex.y;
            float z = vertex.z;
            float s = j / (pointsPerRow - 1.0F);
            vertices.add(new Vector3f(x * radius, y * radius, z * radius));
            normals.add(new Vector3f(x, y, z));
            texCoords.add(new Vector2f(1.0F - s / 2.0F, t / 3.0F + 0.33333334F));
            if (i < pointsPerRow - 1 && j < pointsPerRow - 1) {
               indices.add(k1);
               indices.add(k2);
               indices.add(k1 + 1);
               indices.add(k1 + 1);
               indices.add(k2);
               indices.add(k2 + 1);
            }

            k++;
            j++;
            k1++;
         }
      }

      int vertexSize = pointsPerRow * pointsPerRow;
      int indexSize = 6 * (int)Math.pow(4.0, subdivision);

      for (int i = 0; i < vertexSize; i++) {
         Vector3f vertex = vertices.get(i);
         Vector3f normal = normals.get(i);
         Vector2f texCoord = texCoords.get(i);
         vertices.add(new Vector3f(-vertex.x, vertex.y, -vertex.z));
         normals.add(new Vector3f(-normal.x, normal.y, -normal.z));
         texCoords.add(new Vector2f(texCoord.x + 0.5F, texCoord.y));
      }

      int startIndex = vertexSize;

      for (int i = 0; i < indexSize; i++) {
         indices.add(startIndex + indices.get(i));
      }

      for (int i = 0; i < vertexSize; i++) {
         Vector3f vertex = vertices.get(i);
         Vector3f normal = normals.get(i);
         Vector2f texCoord = texCoords.get(i);
         vertices.add(new Vector3f(-vertex.z, vertex.x, -vertex.y));
         normals.add(new Vector3f(-normal.z, normal.x, -normal.y));
         texCoords.add(new Vector2f(texCoord.x - 0.5F, texCoord.y - 0.33333334F));
      }

      startIndex = vertexSize * 2;

      for (int i = 0; i < indexSize; i++) {
         indices.add(startIndex + indices.get(i));
      }

      for (int i = 0; i < vertexSize; i++) {
         Vector3f vertex = vertices.get(i);
         Vector3f normal = normals.get(i);
         Vector2f texCoord = texCoords.get(i);
         vertices.add(new Vector3f(-vertex.z, -vertex.x, vertex.y));
         normals.add(new Vector3f(-normal.z, -normal.x, normal.y));
         texCoords.add(new Vector2f(texCoord.x, texCoord.y - 0.33333334F));
      }

      startIndex = vertexSize * 3;

      for (int i = 0; i < indexSize; i++) {
         indices.add(startIndex + indices.get(i));
      }

      for (int i = 0; i < vertexSize; i++) {
         Vector3f vertex = vertices.get(i);
         Vector3f normal = normals.get(i);
         Vector2f texCoord = texCoords.get(i);
         vertices.add(new Vector3f(-vertex.z, vertex.y, vertex.x));
         normals.add(new Vector3f(-normal.z, normal.y, normal.x));
         texCoords.add(new Vector2f(texCoord.x, texCoord.y + 0.33333334F));
      }

      startIndex = vertexSize * 4;

      for (int i = 0; i < indexSize; i++) {
         indices.add(startIndex + indices.get(i));
      }

      for (int i = 0; i < vertexSize; i++) {
         Vector3f vertex = vertices.get(i);
         Vector3f normal = normals.get(i);
         Vector2f texCoord = texCoords.get(i);
         vertices.add(new Vector3f(vertex.z, vertex.y, -vertex.x));
         normals.add(new Vector3f(normal.z, normal.y, -normal.x));
         texCoords.add(new Vector2f(texCoord.x + 0.5F, texCoord.y + 0.33333334F));
      }

      startIndex = vertexSize * 5;

      for (int i = 0; i < indexSize; i++) {
         indices.add(startIndex + indices.get(i));
      }

      Matrix4f matrix4f = stack.m_85850_().m_252922_();
      Matrix3f matrix3f = stack.m_85850_().m_252943_();

      for (int i = 0; i < indices.size(); i++) {
         int index = indices.get(i);
         Vector3f vertex = vertices.get(index);
         Vector3f normal = normals.get(index);
         Vector2f uv = texCoords.get(index);
         consumer.m_252986_(matrix4f, vertex.x, vertex.y, vertex.z)
            .m_85950_(1.0F, 1.0F, 1.0F, 1.0F)
            .m_7421_(uv.x, uv.y)
            .m_86008_(overlayTexture)
            .m_85969_(packedLight);
         if (useNormals) {
            consumer.m_252939_(matrix3f, normal.x, normal.y, normal.z).m_5752_();
         } else {
            consumer.m_5601_(0.0F, -1.0F, 0.0F).m_5752_();
         }
      }
   }

   private static List<Vector3f> generateCubeSpherePosXVertices(int pointsPerRow) {
      float D2R = (float)Math.acos(-1.0) / 180.0F;
      List<Vector3f> vertices = Lists.newArrayList();

      for (int i = 0; i < pointsPerRow; i++) {
         float a2 = D2R * (45.0F - 90.0F * i / (pointsPerRow - 1.0F));
         Vector3f n2 = new Vector3f((float)(-Math.sin(a2)), (float)Math.cos(a2), 0.0F);

         for (int j = 0; j < pointsPerRow; j++) {
            float a1 = D2R * (-45.0F + 90.0F * j / (pointsPerRow - 1.0F));
            Vector3f n1 = new Vector3f((float)(-Math.sin(a1)), 0.0F, (float)(-Math.cos(a1)));
            Vector3f v = new Vector3f(n1.y * n2.z - n1.z * n2.y, n1.z * n2.x - n1.x * n2.z, n1.x * n2.y - n1.y * n2.x).normalize();
            vertices.add(v);
         }
      }

      return vertices;
   }

   public static void renderSectorStackSphere(
      float radius, int sectorCount, int stackCount, PoseStack stack, VertexConsumer consumer, int packedLight, int overlayTexture
   ) {
      List<Vector3f> vertices = Lists.newArrayList();
      List<Vector3f> normals = Lists.newArrayList();
      List<Vector2f> texCoords = Lists.newArrayList();
      List<Integer> indices = Lists.newArrayList();
      float lengthInv = 1.0F / radius;
      float sectorStep = (float) (Math.PI * 2) / sectorCount;
      float stackStep = (float) Math.PI / stackCount;

      for (int i = 0; i <= stackCount; i++) {
         float stackAngle = (float) (Math.PI / 2) - i * stackStep;
         float xy = radius * Mth.m_14089_(stackAngle);
         float z = radius * Mth.m_14031_(stackAngle);

         for (int j = 0; j <= sectorCount; j++) {
            float sectorAngle = j * sectorStep;
            float x = xy * Mth.m_14089_(sectorAngle);
            float y = xy * Mth.m_14031_(sectorAngle);
            vertices.add(new Vector3f(x, y, z));
            float nx = x * lengthInv;
            float ny = y * lengthInv;
            float nz = z * lengthInv;
            normals.add(new Vector3f(nx, ny, nz));
            float u = (float)j / sectorCount;
            float v = (float)i / stackCount;
            texCoords.add(new Vector2f(u, v));
         }
      }

      for (int i = 0; i < stackCount; i++) {
         int k1 = i * (sectorCount + 1);
         int k2 = k1 + sectorCount + 1;

         for (int j = 0; j < sectorCount; k2++) {
            if (i != 0) {
               indices.add(k1);
               indices.add(k2);
               indices.add(k1 + 1);
            }

            if (i != stackCount - 1) {
               indices.add(k1 + 1);
               indices.add(k2);
               indices.add(k2 + 1);
            }

            j++;
            k1++;
         }
      }

      Matrix4f matrix4f = stack.m_85850_().m_252922_();
      Matrix3f matrix3f = stack.m_85850_().m_252943_();

      for (int i = 0; i < indices.size(); i++) {
         int index = indices.get(i);
         Vector3f vertex = vertices.get(index);
         Vector3f normal = normals.get(index);
         Vector2f uv = texCoords.get(index);
         consumer.m_252986_(matrix4f, vertex.x, vertex.y, vertex.z)
            .m_85950_(1.0F, 1.0F, 1.0F, 1.0F)
            .m_7421_(uv.x, uv.y)
            .m_86008_(overlayTexture)
            .m_85969_(packedLight)
            .m_252939_(matrix3f, normal.x, normal.y, normal.z)
            .m_5752_();
      }
   }

   public static boolean isMouseInBounds(int mouseX, int mouseY, int x, int y, int width, int height) {
      return mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height;
   }

   public static boolean isMouseInBounds(int mouseX, int mouseY, ScreenRectangle rectangle) {
      return isMouseInBounds(mouseX, mouseY, rectangle.f_263846_().f_263719_(), rectangle.f_263846_().f_263694_(), rectangle.f_263770_(), rectangle.f_263800_());
   }

   public static void adjustProjectionMatrix(float partialTicks, float near, float far) {
      extendFarPlane = true;
      previousProjMat = RenderSystem.getProjectionMatrix();
      Minecraft mc = Minecraft.m_91087_();
      GameRenderer renderer = mc.f_91063_;
      MixinGameRendererAccessor accessor = (MixinGameRendererAccessor)renderer;
      double fov = accessor.crackerslib$getFov(renderer.m_109153_(), partialTicks, true);
      PoseStack stack = new PoseStack();
      stack.m_85850_().m_252922_().identity();
      float zoom = accessor.crackerslib$getZoom();
      if (zoom != 1.0F) {
         stack.m_252880_(accessor.crackerslib$getZoomX(), -accessor.crackerslib$getZoomY(), 0.0F);
         stack.m_85841_(zoom, zoom, 1.0F);
      }

      stack.m_85850_()
         .m_252922_()
         .mul(new Matrix4f().setPerspective((float)(fov * (float) (Math.PI / 180.0)), (float)mc.m_91268_().m_85441_() / mc.m_91268_().m_85442_(), near, far));
      renderer.m_252879_(stack.m_85850_().m_252922_());
   }

   public static void popAdjustedProjectionMatrix() {
      if (previousProjMat == null) {
         throw new NullPointerException("Previous projection matrix is null!");
      } else if (!extendFarPlane) {
         throw new IllegalStateException("Not extending far plane!");
      } else {
         extendFarPlane = false;
         Minecraft.m_91087_().f_91063_.m_252879_(previousProjMat);
         previousProjMat = null;
      }
   }
}

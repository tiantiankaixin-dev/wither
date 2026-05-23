package nonamecrackers2.witherstormmod.client.util;

import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TiledTextureGenerator extends DefaultedVertexConsumer {
   private final VertexConsumer delegate;
   private final PoseStack stack;
   private float x;
   private float y;
   private float z;
   private int r;
   private int g;
   private int b;
   private int a;
   private int overlayU;
   private int overlayV;
   private int lightCoords;
   private float nx;
   private float ny;
   private float nz;
   private float texScale;

   public TiledTextureGenerator(VertexConsumer consumer, PoseStack stack, float texScale) {
      this.delegate = consumer;
      this.stack = stack;
      this.resetState();
      this.texScale = texScale;
   }

   private void resetState() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.r = 255;
      this.g = 255;
      this.b = 255;
      this.a = 255;
      this.overlayU = 0;
      this.overlayV = 10;
      this.lightCoords = 15728880;
      this.nx = 0.0F;
      this.ny = 1.0F;
      this.nz = 0.0F;
   }

   public VertexConsumer vertex(double x, double y, double z) {
      this.x = (float)x;
      this.y = (float)y;
      this.z = (float)z;
      return this;
   }

   public VertexConsumer color(int red, int green, int blue, int alpha) {
      this.r = red;
      this.g = green;
      this.b = blue;
      this.a = alpha;
      return this;
   }

   public VertexConsumer uv(float u, float v) {
      return this;
   }

   public VertexConsumer overlayCoords(int overlayU, int overlayV) {
      this.overlayU = overlayU;
      this.overlayV = overlayV;
      return this;
   }

   public VertexConsumer uv2(int coord1, int coord2) {
      this.lightCoords = coord1 | coord2 << 16;
      return this;
   }

   public VertexConsumer normal(float x, float y, float z) {
      this.nx = x;
      this.ny = y;
      this.nz = z;
      return this;
   }

   public void endVertex() {
      Vector3f vector3f = new Matrix3f(this.stack.last().normal()).invert().transform(new Vector3f(this.nx, this.ny, this.nz));
      Direction direction = Direction.getNearest(vector3f.x(), vector3f.y(), vector3f.z());
      Vector4f vector4f = new Matrix4f(this.stack.last().pose()).invert().transform(new Vector4f(this.x, this.y, this.z, 1.0F));
      vector4f.rotateY((float) Math.PI);
      vector4f.rotateX((float) (-Math.PI / 2));
      vector4f.rotate(direction.getRotation());
      float f = ((direction.getAxis() == Axis.X ? -1.0F : 1.0F) * vector4f.x() + 0.5F) * this.texScale + this.getUOffset(direction);
      float f1 = (vector4f.y() + 0.5F) * this.texScale + this.getVOffset(direction);
      this.delegate
         .vertex((double)this.x, (double)this.y, (double)this.z)
         .color(this.r, this.g, this.b, this.a)
         .uv(f, f1)
         .overlayCoords(this.overlayU, this.overlayV)
         .uv2(this.lightCoords)
         .normal(this.nx, this.ny, this.nz)
         .endVertex();
      this.resetState();
   }

   private float getUOffset(Direction direction) {
      Vector3f step = direction.step();
      if (step.x > 0.0F || step.x < 0.0F) {
         return 0.0F;
      } else if (step.z > 0.0F || step.z < 0.0F) {
         return this.texScale;
      } else {
         return !(step.y > 0.0F) && !(step.y < 0.0F) ? 0.0F : this.texScale * 2.0F;
      }
   }

   private float getVOffset(Direction direction) {
      Vector3f step = direction.step();
      if (step.x < 0.0F || step.z < 0.0F || step.y < 0.0F) {
         return 0.0F;
      } else {
         return !(step.x > 0.0F) && !(step.z > 0.0F) && !(step.y > 0.0F) ? 0.0F : this.texScale * 2.0F;
      }
   }
}

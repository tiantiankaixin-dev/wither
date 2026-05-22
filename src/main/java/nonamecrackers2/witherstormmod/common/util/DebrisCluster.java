package nonamecrackers2.witherstormmod.common.util;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;

public class DebrisCluster {
   private final float orbitalAngleOffset;
   private final float verticalOffset;
   private final float radiusFromCenter;
   private final float speed;
   private final float sizeModifier;
   private int renderPhase;
   private final List<DebrisCluster.Piece> pieces = Lists.newArrayList();
   private Vec2 rotationMotion = Vec2.ZERO;
   private float xRot;
   private float xRotO;
   private float yRot;
   private float yRotO;
   private float orbitalAngle;
   private float orbitalAngleO;
   private boolean isDisabled;
   private boolean isGlowing;
   private boolean isForcedGlowing;

   public DebrisCluster(float orbitalAngleOffset, float verticalOffset, float radiusFromCenter, float startingSpeed, float sizeModifier) {
      this.orbitalAngleOffset = orbitalAngleOffset;
      this.verticalOffset = verticalOffset;
      this.radiusFromCenter = radiusFromCenter;
      this.speed = startingSpeed;
      this.sizeModifier = sizeModifier;
   }

   public void randomize(RandomSource random, int pieceCount, float spread) {
      this.xRot = random.nextFloat() * 360.0F;
      this.xRotO = this.xRot;
      this.yRot = random.nextFloat() * 360.0F;
      this.yRotO = this.yRot;
      this.rotationMotion = new Vec2(random.nextFloat() * 10.0F - 5.0F, random.nextFloat() * 10.0F - 5.0F);
      this.pieces.clear();

      for (int i = 0; i < pieceCount; i++) {
         float x = random.nextFloat() * spread * 2.0F - spread;
         float y = random.nextFloat() * spread * 2.0F - spread;
         float z = random.nextFloat() * spread * 2.0F - spread;
         float size = (0.3F + random.nextFloat() * 0.3F) * this.sizeModifier;
         this.pieces.add(new DebrisCluster.Piece(x, y, z, size));
      }

      this.isGlowing = random.nextInt(20) == 0;
   }

   public void determineRenderPhase() {
      if (this.radiusFromCenter > 80.0F) {
         this.renderPhase = 6;
      } else if (this.verticalOffset < 60.0F) {
         this.renderPhase = 4;
      } else if (this.verticalOffset < 80.0F) {
         this.renderPhase = 5;
      } else {
         this.renderPhase = 6;
      }
   }

   public void setRenderPhase(int phase) {
      this.renderPhase = phase;
   }

   public void tick() {
      this.xRotO = this.xRot;
      this.yRotO = this.yRot;
      this.orbitalAngleO = this.orbitalAngle;
      this.orbitalAngle = this.orbitalAngle + this.speed;
      this.xRot = this.xRot + this.rotationMotion.x;
      this.yRot = this.yRot + this.rotationMotion.y;
   }

   public float getOrbitalAngle(float partialTick) {
      return Mth.lerp(partialTick, this.orbitalAngleO, this.orbitalAngle) + this.orbitalAngleOffset;
   }

   public float getVerticalOffset() {
      return this.verticalOffset;
   }

   public float getRadiusFromCenter() {
      return this.radiusFromCenter;
   }

   public float getXRot(float partialTick) {
      return Mth.lerp(partialTick, this.xRotO, this.xRot);
   }

   public float getYRot(float partialTick) {
      return Mth.lerp(partialTick, this.yRotO, this.yRot);
   }

   public boolean isDisabled() {
      return this.isDisabled;
   }

   public void setDisabled(boolean flag) {
      this.isDisabled = flag;
   }

   public void setGlowing(boolean flag) {
      this.isForcedGlowing = flag;
   }

   public List<DebrisCluster.Piece> getPieces() {
      return this.pieces;
   }

   public boolean isGlowing() {
      return this.isGlowing;
   }

   public boolean isForcedGlowing() {
      return this.isForcedGlowing;
   }

   public int getRenderPhase() {
      return this.renderPhase;
   }

   public static record Piece(float x, float y, float z, float size) {
   }
}

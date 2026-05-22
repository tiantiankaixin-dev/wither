package nonamecrackers2.witherstormmod.common.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.world.phys.Vec2;

public class LegacyDebrisChunks {
   private final List<LegacyDebrisChunks.DebrisChunk> chunks;
   private final float radius;
   private final float yOffset;
   private final int phase;

   public LegacyDebrisChunks(boolean hidden, int amount, float radius, float yOffset, int verticalOffset, int distanceFromCenter, float boundary, int phase) {
      List<LegacyDebrisChunks.DebrisChunk> debris = new ArrayList<>();

      for (int i = 0; i < amount; i++) {
         Random debrisRandom = new Random();
         LegacyDebrisChunks.DebrisChunk chunk = new LegacyDebrisChunks.DebrisChunk(debrisRandom, verticalOffset, distanceFromCenter);
         chunk.disabled = hidden;
         debris.add(chunk);
      }

      this.chunks = debris;
      this.radius = radius;
      this.yOffset = yOffset;
      this.phase = phase;
      this.clean();
   }

   public LegacyDebrisChunks(boolean hidden, int amount, float radius, float yOffset, int phase) {
      this(hidden, amount, radius, yOffset, 80, 20, 0.3F, phase);
   }

   private void clean() {
      List<LegacyDebrisChunks.DebrisChunk> chunks = Lists.newArrayList(this.chunks);

      for (int i = 0; i < chunks.size(); i++) {
         LegacyDebrisChunks.DebrisChunk chunk = chunks.get(i);
         if (chunk.distanceFromCenter < 0.3F) {
            this.chunks.remove(chunk);
         }
      }
   }

   public List<LegacyDebrisChunks.DebrisChunk> getChunks() {
      return this.chunks;
   }

   public float getRadius() {
      return this.radius;
   }

   public float getYOffset() {
      return this.yOffset;
   }

   public int getRequiredPhase() {
      return this.phase;
   }

   public static class DebrisChunk {
      private final Random random;
      private final float orbitOffset;
      private final float distanceFromCenter;
      private final float verticalOffset;
      private final float radius;
      private final Vec2 motion;
      public float speed;
      public float xRot;
      public float yRot;
      public float xRotO;
      public float yRotO;
      public float orbitalAngle;
      public float orbitalAngleO;
      public boolean disabled;

      public DebrisChunk(Random random) {
         this(random, 80, 20);
      }

      public DebrisChunk(Random random, int verticalOffset, int distanceFromCenter) {
         this.random = random;
         this.orbitOffset = (float)this.random.nextInt(360);
         this.distanceFromCenter = (float)((double)this.random.nextInt(distanceFromCenter) * 0.1 / 2.0);
         this.verticalOffset = (float)(this.random.nextInt(verticalOffset) / 2);
         this.radius = Math.max(0.0F, this.random.nextFloat() - 0.5F);
         this.motion = new Vec2((float)((this.random.nextInt(60) - 30) / 2), (float)((this.random.nextInt(60) - 30) / 2));
         this.speed = this.random.nextFloat() * 6.0F - 3.0F;
      }

      public float getOrbitOffset() {
         return this.orbitOffset;
      }

      public float distanceFromCenter() {
         return this.distanceFromCenter;
      }

      public float verticalOffset() {
         return this.verticalOffset;
      }

      public float getRadius() {
         return this.radius;
      }

      public Vec2 getMotion() {
         return this.motion;
      }

      public void setRotation(float x, float y) {
         this.xRotO = this.xRot;
         this.yRotO = this.yRot;
         this.xRot = x % 360.0F;
         this.yRot = y % 360.0F;
      }

      public void setOrbitalAngle(float angle) {
         this.orbitalAngleO = this.orbitalAngle;
         this.orbitalAngle = angle % 360.0F;
      }
   }
}
